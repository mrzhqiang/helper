package helper.database.cassandra.internal;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.QueryLogger;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.google.common.base.Preconditions;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import helper.database.cassandra.Cassandra;
import helper.database.internal.Util;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.datastax.driver.core.ProtocolOptions.*;
import static com.datastax.driver.core.querybuilder.QueryBuilder.*;

/**
 * 单机版 Cassandra 驱动。
 *
 * @author qiang.zhang
 */
@Singleton
public final class StandaloneCassandra implements Cassandra {
  private static final Logger LOGGER = LoggerFactory.getLogger("cassandra");

  private static final String ROOT_PATH = "cassandra";

  private static final String ENABLED = "enabled";
  private static final String HOST = "host";
  private static final String PORT = "port";
  private static final String MAX_SECONDS = "max-seconds";

  private static final String LOCALHOST = "localhost";

  private boolean enabled = false;
  private Cluster cluster;
  private MappingManager mappingManager;

  public StandaloneCassandra() {
    Config config = ConfigFactory.load();
    if (!config.hasPath(ROOT_PATH)) {
      return;
    }

    Config root = config.getConfig(ROOT_PATH);
    enabled = root.hasPath(ENABLED) && root.getBoolean(ENABLED);
    if (!enabled) {
      LOGGER.error("you want use Cassandra but it not be enabled.");
      return;
    }

    String host = root.hasPath(HOST) ? root.getString(HOST) : LOCALHOST;
    int port = root.hasPath(PORT) ? root.getInt(PORT) : DEFAULT_PORT;
    int maxSeconds = root.hasPath(MAX_SECONDS) ? root.getInt(MAX_SECONDS)
        : DEFAULT_MAX_SCHEMA_AGREEMENT_WAIT_SECONDS;

    cluster = Util.create(() -> Cluster.builder()
        .addContactPoint(host)
        .withPort(port)
        .withMaxSchemaAgreementWaitSeconds(maxSeconds)
        .build()
        .register(QueryLogger.builder().build()));
    mappingManager = Util.create(() -> new MappingManager(cluster.newSession()));
    LOGGER.info("Cassandra create successful.");

    Config check = root.getConfig("check");
    execute(session -> {
      List<String> columns = check.getStringList("columns");
      String keyspace = check.getString("keyspace");
      String table = check.getString("table");
      Row row = session.execute(select(columns.toArray()).from(keyspace, table)).one();
      String status = columns.stream()
          .map(s -> s + ":" + row.getObject(s))
          .collect(Collectors.toList()).toString();
      LOGGER.info("Cassandra check:{}", status);
    });
  }

  @Override public void execute(Consumer<Session> consumer) {
    if (!enabled || cluster == null) {
      LOGGER.warn("Cassandra is disabled.");
      return;
    }

    Preconditions.checkNotNull(consumer);
    try (Session session = cluster.connect()) {
      Util.execute(session, consumer);
    }
  }

  @Override public <T> Optional<Mapper<T>> mapper(Class<T> clazz) {
    if (!enabled || mappingManager == null) {
      LOGGER.warn("Cassandra is disabled.");
      return Optional.empty();
    }

    Preconditions.checkNotNull(clazz);
    return Optional.ofNullable(Util.create(() -> mappingManager.mapper(clazz)));
  }
}
