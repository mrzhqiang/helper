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
import java.util.Optional;
import java.util.function.Consumer;
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
  private static final String MAX_SECONDS = "maxSeconds";

  private static final String DEFAULT_HOST = "localhost";
  private static final String CLUSTER_NAME = "cluster_name";
  private static final String RELEASE_VERSION = "release_version";

  private boolean enabled = false;
  private Cluster cluster;
  private MappingManager mappingManager;

  public StandaloneCassandra() {
    Config config = ConfigFactory.load();
    if (!config.hasPath(ROOT_PATH)) {
      return;
    }

    Config rootConfig = config.getConfig(ROOT_PATH);
    enabled = rootConfig.hasPath(ENABLED) && rootConfig.getBoolean(ENABLED);
    if (!enabled) {
      LOGGER.info("you want use Cassandra but it not be enabled.");
      return;
    }

    String host = rootConfig.hasPath(HOST) ? rootConfig.getString(HOST) : DEFAULT_HOST;
    int port = rootConfig.hasPath(PORT) ? rootConfig.getInt(PORT) : DEFAULT_PORT;
    int maxSeconds = rootConfig.hasPath(MAX_SECONDS) ?
        rootConfig.getInt(MAX_SECONDS) : DEFAULT_MAX_SCHEMA_AGREEMENT_WAIT_SECONDS;

    cluster = Util.create(() -> Cluster.builder()
        .addContactPoint(host)
        .withPort(port)
        .withMaxSchemaAgreementWaitSeconds(maxSeconds)
        .build()
        .register(QueryLogger.builder().build()));
    mappingManager = Util.create(() -> new MappingManager(cluster.newSession()));
    LOGGER.info("Cassandra create successful.");

    execute(session -> {
      Row row = session.execute(
          select(CLUSTER_NAME, RELEASE_VERSION)
              .from("system", "local")
      ).one();
      LOGGER.info("Cassandra cluster_name:{} release_version:{}.",
          row.getString(CLUSTER_NAME),
          row.getString(RELEASE_VERSION));
    });
    LOGGER.info("Cassandra is normal.");
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
