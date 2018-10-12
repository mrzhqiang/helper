package helper;

import com.google.inject.ImplementedBy;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import org.elasticsearch.client.RestClient;

/**
 * Elasticsearch 客户端。
 *
 * @author qiang.zhang
 */
@ImplementedBy(StandaloneElasticsearch.class)
public interface Elasticsearch {
  /**
   * 执行 PUT、DELETE 等请求用于创建索引和类型，以及删除某些资源。
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
