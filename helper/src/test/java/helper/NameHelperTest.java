package helper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author mrzhqiang
 */
public final class NameHelperTest {
  @Test
  public void firstLetter() {
    String defaultLetter = NameHelper.firstLetter("");
    assertNotNull(defaultLetter);
    assertEquals("m", defaultLetter);

    String mrzhqiang = NameHelper.firstLetter("play-home");
    assertNotNull(mrzhqiang);
    assertEquals("p", mrzhqiang);
  }

  @Test
  public void color() {
    int color = NameHelper.color("mrzhqiang");
    // 0xff3d00
    assertEquals(16727296, color);
  }

  @Test
  public void checkName() {
    assertTrue(NameHelper.checkName("中2"));
  }

  @Test
  public void checkNameAndLength() {
    assertTrue(NameHelper.checkName("中国No1", 2, 10));
  }

  @Test
  public void checkChinese() {
    assertFalse(NameHelper.checkChinese("false"));
  }

  @Test
  public void checkChineseAndLength() {
    assertFalse(NameHelper.checkChinese("中华人民共和国", 1, 4));
  }
}