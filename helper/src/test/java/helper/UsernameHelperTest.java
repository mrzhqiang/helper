package helper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author mrzhqiang
 */
public final class UsernameHelperTest {
  @Test
  public void firstLetter() {
    String defaultLetter = UsernameHelper.firstLetter("");
    assertNotNull(defaultLetter);
    assertEquals("m", defaultLetter);

    String mrzhqiang = UsernameHelper.firstLetter("play-home");
    assertNotNull(mrzhqiang);
    assertEquals("p", mrzhqiang);
  }

  @Test
  public void color() {
    int color = UsernameHelper.color("mrzhqiang");
    assertEquals(-12627531, color);
  }

  @Test
  public void checkName() {
    assertTrue(UsernameHelper.checkName("中2"));
  }

  @Test
  public void checkNameAndLength() {
    assertTrue(UsernameHelper.checkName("中国No1", 2, 10));
  }

  @Test
  public void checkChinese() {
    assertFalse(UsernameHelper.checkChinese("false"));
  }

  @Test
  public void checkChineseAndLength() {
    assertFalse(UsernameHelper.checkChinese("中华人民共和国", 1, 4));
  }
}