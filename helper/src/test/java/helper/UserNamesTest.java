package helper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author mrzhqiang
 */
public final class UserNamesTest {
  @Test
  public void firstLetter() {
    String defaultLetter = UserNames.firstLetter("");
    assertNotNull(defaultLetter);
    assertEquals("m", defaultLetter);

    String mrzhqiang = UserNames.firstLetter("play-home");
    assertNotNull(mrzhqiang);
    assertEquals("p", mrzhqiang);
  }

  @Test
  public void color() {
    String color = UserNames.color("mrzhqiang");
    // 0xff3d00
    assertEquals("0xff3d00", color);
  }

  @Test
  public void checkName() {
    assertTrue(UserNames.checkName("中2"));
  }

  @Test
  public void checkNameAndLength() {
    assertTrue(UserNames.checkName("中国No1", 2, 10));
  }

  @Test
  public void checkChinese() {
    assertFalse(UserNames.checkChinese("false"));
  }

  @Test
  public void checkChineseAndLength() {
    assertFalse(UserNames.checkChinese("中华人民共和国", 1, 4));
  }
}