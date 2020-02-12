package helper.data.elasticsearch.internal;

import com.google.common.base.Preconditions;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import helper.data.Util;
import helper.data.elasticsearch.Elasticsearch;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

/**
 * 单机版 Elasticsearch 客户端。
 *
 * @author qiang.zhang
 */
@Slf4j
@Singleton
public final class StandaloneElasticsearch implements Elasticsearch {
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

    Config root = config.getConfig(ROOT_PATH);
    enabled = root.hasPath(ENABLED) && root.getBoolean(ENABLED);
    if (!enabled) {
      log.error("you want use Elasticsearch but it not be enabled.");
      return;
    }

    String host = root.hasPath(HOST) ? root.getString(HOST) : DEFAULT_HOST;
    int port = root.hasPath(PORT) ? root.getInt(PORT) : DEFAULT_PORT;
    String scheme = root.hasPath(SCHEME) ? root.getString(SCHEME) : DEFAULT_SCHEME;
    restClient = Util.create(() -> RestClient.builder(new HttpHost(host, port, scheme)).build());
    log.info("Elasticsearch client create successful.");
    execute(restClient -> {
      try {
        Response response = restClient.performRequest(HttpGet.METHOD_NAME, "//");
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
          String result = EntityUtils.toString(response.getEntity());
          log.debug(result);
        }
      } catch (IOException e) {
        log.error("Elasticsearch request error, cause: {}", e.getMessage());
      }
    });
  }

  @Override public void execute(Consumer<RestClient> consumer) {
    if (!enabled || restClient == null) {
      log.warn("Elasticsearch is disabled.");
      return;
    }

    Preconditions.checkNotNull(consumer);
    Util.execute(restClient, consumer);
  }

  @Override public <T> Optional<T> find(Function<RestClient, T> function) {
    if (!enabled || restClient == null) {
      log.warn("Elasticsearch is disabled.");
      return Optional.empty();
    }

    Preconditions.checkNotNull(function);
    return Util.create(() -> Optional.ofNullable(function.apply(restClient)));
  }
}
