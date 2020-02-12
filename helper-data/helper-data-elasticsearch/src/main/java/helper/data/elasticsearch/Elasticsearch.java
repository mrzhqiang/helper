package helper.data.elasticsearch;

import com.google.inject.ImplementedBy;
import helper.data.elasticsearch.internal.StandaloneElasticsearch;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import org.elasticsearch.client.RestClient;

/**
 * Elasticsearch 客户端。
 * <p>
 * 目前只有单机版，事实上 Elasticsearch 客户端只需要单机版。
 * <p>
 * 要启用 Elasticsearch，请在模块下增加 reference.conf 文件，或在项目下增加 application.conf 文件，然后
 * 设定：
 * <pre>
 *   elasticsearch.enabled = true
 * </pre>
 * 即可启动。
 * <p>
 * 若想设定不同的配置，比如主机地址、端口，可以这样做：
 * <pre>
 *   elasticsearch.host = "192.168.1.100"
 *   elasticsearch.port = 9201
 * </pre>
 * 其中，192.168.1.100 和 9201 必须是 Elasticsearch 数据库实例的主机地址和端口。
 * <p>
 * 注意：Elasticsearch 实际上不作为数据库使用，因为它的 API 过于灵活而显得复杂，改造为通用数据库
 * 逻辑需要花一点时间，所以我们把它留到最后完成。
 *
 * @author qiang.zhang
 */
@ImplementedBy(StandaloneElasticsearch.class)
public interface Elasticsearch {
  /**
   * 执行 PUT、DELETE 等请求，用于创建索引和类型，以及删除某些资源。
   * <p>
   * 不需要自动释放 {@link RestClient}。
   *
   * @param consumer 包裹 {@link RestClient} 的消费者对象。
   */
  void execute(Consumer<RestClient> consumer);

  /**
   * 查询指定的对象。
   *
   * @param function 功能对象。
   * @param <T> 指定类型。
   * @return 可选的对象。
   */
  <T> Optional<T> find(Function<RestClient, T> function);
}
