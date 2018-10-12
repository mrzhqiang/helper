package helper;

import com.google.common.base.Preconditions;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

/**
 * 单机版 Elasticsearch 客户端。
 *
 * @author qiang.zhang
 */
@Singleton final class StandaloneElasticsearch implements Elasticsearch {
  private static final Logger LOGGER = LogManager.getLogger("database");

  private static final String ROOT_PATH = "elasticsearch";
  private static final String HOST = "host";
  private static final String PORT = "port";
  private static final String SCHEME = "scheme";
  private static final String ENABLED = "enabled";

  private static final String DEFAULT_HOST = "localhost";
  private static final int DEFAULT_PORT = 9200;
  private static final String DEFAULT_SCHEME = HttpHost.DEFAULT_SCHEME_NAME;

  private boolean enabled = false;
  private RestClient restClient;

  public StandaloneElasticsearch() {
    Config config = ConfigFactory.load();
    if (!config.hasPath(ROOT_PATH)) {
      return;
    }

    Config rootConfig = config.getConfig(ROOT_PATH);
    enabled = rootConfig.hasPath(ENABLED) && rootConfig.getBoolean(ENABLED);
    if (!enabled) {
      LOGGER.info("Elasticsearch is not enabled.");
      return;
    }

    String host = rootConfig.hasPath(HOST) ? rootConfig.getString(HOST) : DEFAULT_HOST;
    int port = rootConfig.hasPath(PORT) ? rootConfig.getInt(PORT) : DEFAULT_PORT;
    String scheme = rootConfig.hasPath(SCHEME) ? rootConfig.getString(SCHEME) : DEFAULT_SCHEME;
    restClient = DatabaseHelper.create(() ->
        RestClient.builder(new HttpHost(host, port, scheme)).build());
    execute(restClient -> {
      try {
        Response response = restClient.performRequest(HttpGet.METHOD_NAME, "//");
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
          String result = EntityUtils.toString(response.getEntity());
          LOGGER.info(result);
        }
      } catch (IOException e) {
        throw new RuntimeException("rest client request error.", e);
      }
      LOGGER.info("Elasticsearch is normal.");
    });
  }

  @Override public void execute(Consumer<RestClient> consumer) {
    if (!enabled) {
      LOGGER.warn("Elasticsearch is disabled.");
      return;
    }

    Preconditions.checkNotNull(consumer);
    DatabaseHelper.execute(restClient, consumer);
  }

  @Override public <T> Optional<T> find(Function<RestClient, T> function) {
    if (!enabled) {
      LOGGER.warn("Elasticsearch is disabled.");
      return Optional.empty();
    }

    Preconditions.checkNotNull(function);
    return DatabaseHelper.create(() -> Optional.ofNullable(function.apply(restClient)));
  }
}
