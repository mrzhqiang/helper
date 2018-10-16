package database;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ProtocolOptions;
import com.datastax.driver.core.QueryLogger;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.google.common.base.Preconditions;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.datastax.driver.core.querybuilder.QueryBuilder.select;

/**
 * 单机版 Cassandra 驱动。
 *
 * @author qiang.zhang
 */
@Singleton final class StandaloneCassandra implements Cassandra {
  private static final Logger LOGGER = LogManager.getLogger("database");

  private static final String ROOT_PATH = "cassandra";
  private static final String HOST = "host";
  private static final String PORT = "port";
  private static final String MAX_SECONDS = "maxSeconds";
  private static final String ENABLED = "enabled";

  private static final String DEFAULT_HOST = "localhost";
  private static final int DEFAULT_PORT = ProtocolOptions.DEFAULT_PORT;
  private static final int DEFAULT_TIMEOUT =
      ProtocolOptions.DEFAULT_MAX_SCHEMA_AGREEMENT_WAIT_SECONDS;

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

    Config root = config.getConfig(ROOT_PATH);
    enabled = root.hasPath(ENABLED) && root.getBoolean(ENABLED);
    if (!enabled) {
      LOGGER.info("Cassandra is not enabled.");
      return;
    }

    String host = root.hasPath(HOST) ? root.getString(HOST) : DEFAULT_HOST;
    int port = root.hasPath(PORT) ? root.getInt(PORT) : DEFAULT_PORT;
    int maxSeconds = root.hasPath(MAX_SECONDS) ? root.getInt(MAX_SECONDS) : DEFAULT_TIMEOUT;

    cluster = Databases.create(() -> Cluster.builder()
        .addContactPoint(host)
        .withPort(port)
        .withMaxSchemaAgreementWaitSeconds(maxSeconds)
        .build()
        .register(QueryLogger.builder().build()));
    mappingManager = Databases.create(() -> new MappingManager(cluster.newSession()));
    LOGGER.info("Cassandra cluster and mapping manager create successful.");

    execute(session -> {
      Row row = session.execute(
          select(CLUSTER_NAME, RELEASE_VERSION)
              .from("system", "local")
      ).one();
      LOGGER.info("Cassandra cluster_name:{} release_version:{}.",
          row.getString(CLUSTER_NAME),
          row.getString(RELEASE_VERSION));
      LOGGER.info("Cassandra is normal.");
    });
  }

  @Override public void execute(Consumer<Session> consumer) {
    if (!enabled) {
      LOGGER.warn("Cassandra is disabled.");
      return;
    }

    Preconditions.checkNotNull(consumer);
    try (Session session = cluster.connect()) {
      Databases.execute(session, consumer);
    }
  }

  @Nullable
  @Override public <E> Mapper<E> mapper(Class<E> clazz) {
    if (!enabled) {
      LOGGER.warn("Cassandra is disabled.");
      return null;
    }

    Preconditions.checkNotNull(clazz);
    return Databases.create(() -> mappingManager.mapper(clazz));
  }
}
