package com.github.mrzhqiang.helper.data.cassandra.internal;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.QueryLogger;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.github.mrzhqiang.helper.data.cassandra.Cassandra;
import com.github.mrzhqiang.helper.data.util.Datas;
import com.google.common.base.Preconditions;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.datastax.driver.core.ProtocolOptions.DEFAULT_MAX_SCHEMA_AGREEMENT_WAIT_SECONDS;
import static com.datastax.driver.core.ProtocolOptions.DEFAULT_PORT;
import static com.datastax.driver.core.querybuilder.QueryBuilder.select;

/**
 * 单机版 Cassandra 驱动。
 *
 * @author qiang.zhang
 */
@Slf4j(topic = "helper.data.cassandra")
@Singleton
public final class StandaloneCassandra implements Cassandra {

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
            log.error("you want use Cassandra but it not be enabled.");
            return;
        }

        String host = root.hasPath(HOST) ? root.getString(HOST) : LOCALHOST;
        int port = root.hasPath(PORT) ? root.getInt(PORT) : DEFAULT_PORT;
        int maxSeconds = root.hasPath(MAX_SECONDS) ? root.getInt(MAX_SECONDS)
                : DEFAULT_MAX_SCHEMA_AGREEMENT_WAIT_SECONDS;

        cluster = Datas.get(() -> Cluster.builder()
                .addContactPoint(host)
                .withPort(port)
                .withMaxSchemaAgreementWaitSeconds(maxSeconds)
                .build()
                .register(QueryLogger.builder().build()));
        mappingManager = Datas.get(() -> new MappingManager(cluster.newSession()));
        log.info("Cassandra create successful.");

        Config check = root.getConfig("check");
        execute(session -> {
            List<String> columns = check.getStringList("columns");
            String keyspace = check.getString("keyspace");
            String table = check.getString("table");
            Row row = session.execute(select(columns.toArray()).from(keyspace, table)).one();
            String status = columns.stream()
                    .map(s -> s + ":" + row.getObject(s))
                    .collect(Collectors.toList()).toString();
            log.info("Cassandra check:{}", status);
        });
    }

    @Override
    public void execute(Consumer<Session> consumer) {
        if (!enabled || Objects.isNull(cluster)) {
            log.warn("Cassandra is disabled.");
            return;
        }

        Preconditions.checkNotNull(consumer, "consumer == null");
        try (Session session = cluster.connect()) {
            Datas.accept(session, consumer);
        }
    }

    @Override
    public <T> Mapper<T> mapper(Class<T> clazz) {
        if (!enabled || Objects.isNull(mappingManager)) {
            log.warn("Cassandra is disabled.");
            return null;
        }

        Preconditions.checkNotNull(clazz, "clazz == null");
        return Datas.get(() -> mappingManager.mapper(clazz));
    }
}
