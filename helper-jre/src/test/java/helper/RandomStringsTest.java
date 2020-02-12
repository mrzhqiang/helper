package helper;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author mrzhqiang
 */
public final class RandomStringsTest {

  private Config chinese;

  @Before
  public void setUp() {
    chinese = ConfigFactory.load().getConfig("helper.random.chinese");
  }

  @Test
  public void usernameOf() {
    String s = RandomStrings.ofLength(32);
    assertEquals(32, s.length());
    System.out.println(s);
  }

  @Test
  public void usernameOf1() {
    String s = RandomStrings.ofLength(5, 10);
    assertTrue(s.length() >= 5 && s.length() < 10);
    System.out.println(s);
  }

  @Test
  public void chineseOf() {
    String chinese = RandomStrings.ofChinese(5);
    assertEquals(5, chinese.length());
    System.out.println(chinese);
  }

  @Test
  public void chineseOf1() {
    String s = RandomStrings.ofChinese(5, 10);
    assertTrue(s.length() >= 5 && s.length() < 10);
    System.out.println(s);
  }

  @Test
  public void surname() {
    String surname = RandomStrings.ofSurname();
    List<String> strings = chinese.getStringList("surname");
    assertTrue(strings.contains(surname));
  }

  @Test
  public void numberOf() {
    String s = RandomStrings.ofNumber(5);
    assertEquals(5, s.length());
    System.out.println(s);
  }

  @Test
  public void numberOf1() {
    String s = RandomStrings.ofNumber(5, 10);
    assertTrue(s.length() >= 5 && s.length() < 10);
    System.out.println(s);
  }

  @Test
  public void lowerCaseOf() {
    String s = RandomStrings.ofLowerCase(5);
    assertEquals(5, s.length());
    System.out.println(s);
  }

  @Test
  public void lowerCaseOf1() {
    String s = RandomStrings.ofLowerCase(5, 10);
    assertTrue(s.length() >= 5 && s.length() < 10);
    System.out.println(s);
  }

  @Test
  public void upperCaseOf() {
    String s = RandomStrings.ofUpperCase(5);
    assertEquals(5, s.length());
    System.out.println(s);
  }

  @Test
  public void upperCaseOf1() {
    String s = RandomStrings.ofUpperCase(5, 10);
    assertTrue(s.length() >= 5 && s.length() < 10);
    System.out.println(s);
  }
}