package helper;

/**
 * Token 助手。
 * <p>
 * 这个助手的定义基于：
 * <pre>
 *   https://www.oauth.com/oauth2-servers/access-tokens/access-token-response/
 * </pre>
 *
 * 规则：
 *
 * 1. 授权码。使用授权码 + 重定向 URI + 客户端 ID 或 HTTP Basic Auth 或其他方式验证后，
 * 换取有限访问权限的 Token，通常由第三方程序发起请求。应采用 Base64 进行简单加密。
 *
 * 2.
 *
 * @author qiang.zhang
 */
public final class TokenHelper {
  private TokenHelper() {
    throw new AssertionError("No instance.");
  }



}
