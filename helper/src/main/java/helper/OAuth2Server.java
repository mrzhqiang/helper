package helper;

import java.time.Duration;
import javax.annotation.Nullable;

/**
 * 授权服务。
 * <p>
 * 参考：
 * <pre>
 *   https://www.oauth.com/
 * </pre>
 * <p>
 * 注意：目前不准备实现第三方授权登录。
 *
 * @author qiang.zhang
 */
public interface OAuth2Server {
  /**
   * HS256 加密算法必须满足的秘钥最小长度。
   */
  int SECRET_HS256_MIN_LENGTH = 32;
  /**
   * 规定 HS256 加密算法的秘钥最大长度。
   * <p>
   * 提示：主要是为了节省计算量。
   */
  int SECRET_HS256_MAX_LENGTH = 64;


  /**
   * 授权类型。（需要）
   */
  String KEY_GRANT_TYPE = "grant_type";
  /**
   * 授权码键名。（需要）
   */
  String KEY_CODE = "code";
  /**
   * 授权码请求。
   */
  String GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code";
  /**
   * 访问令牌键名。（必须）
   */
  String KEY_ACCESS_TOKEN = "access_token";
  /**
   * 令牌类型键名。（必须）
   */
  String KEY_TOKEN_TYPE = "token_type";
  /**
   * 有效时间键名。（推荐）
   */
  String KEY_EXPIRES_IN = "expires_in";
  /**
   * 刷新令牌键名。（可选）
   */
  String KEY_REFRESH_TOKEN = "refresh_token";
  /**
   * 默认的令牌类型。
   * <p>
   * 目前只有持票人这一种。
   */
  String DEFAULT_TOKEN_TYPE = "bearer";
  /**
   * 令牌的过期时间：365 天。（可选）
   * <p>
   * 理由：无限期的刷新令牌非常消耗服务器资源。
   */
  long TOKEN_EXPIRES_IN = Duration.ofDays(365).getSeconds();

  /**
   * 访问令牌的长度，理论上来说，140 位长度已
   */
  int ACCESS_TOKEN_LENGTH = 140;
  int REFRESH_TOKEN_LENGTH = 140;

  /**
   * 通过设备号，生成给 JWT 使用的秘钥。
   *
   * @param deviceId 客户端设备号。允许 Null 值，这将表示 Web 端；其他还有：android_id、UUID，等等。
   *     区分设备号有助于降低秘钥的重复率，并且对于相同设备秘钥被覆盖的情况，则触发重新登录机制。
   * @return 安全秘钥。
   */
  String secretIdBy(@Nullable String deviceId);

}
