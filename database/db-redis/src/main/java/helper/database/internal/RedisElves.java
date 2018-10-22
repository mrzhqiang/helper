package helper.database.internal;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import helper.database.Entity;
import helper.database.Paging;
import helper.database.internal.Util;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import redis.clients.jedis.Jedis;

/**
 * Redis 客户端精灵。
 * <p>
 * 辅助完成一系列转换过程。
 *
 * @author mrzhqiang
 */
public final class RedisElves {
  private RedisElves() {
    throw new AssertionError("No instance.");
  }

}
