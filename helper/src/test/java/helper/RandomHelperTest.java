package helper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author mrzhqiang
 */
public final class RandomHelperTest {
  @Test
  public void usernameOf() {
    String s = RandomHelper.getString(32);
    assertEquals(32, s.length());
    System.out.println(s);
  }

  @Test
  public void usernameOf1() {
    String s = RandomHelper.getString(5, 10);
    assertTrue(s.length() >= 5 && s.length() < 10);
    System.out.println(s);
  }

  @Test
  public void numberOf() {
    String s = RandomHelper.getNumber(5);
    assertEquals(5, s.length());
    System.out.println(s);
  }

  @Test
  public void numberOf1() {
    String s = RandomHelper.getNumber(5, 10);
    assertTrue(s.length() >= 5 && s.length() < 10);
    System.out.println(s);
  }

  @Test
  public void lowerCaseOf() {
    String s = RandomHelper.getLowerCase(5);
    assertEquals(5, s.length());
    System.out.println(s);
  }

  @Test
  public void lowerCaseOf1() {
    String s = RandomHelper.getLowerCase(5, 10);
    assertTrue(s.length() >= 5 && s.length() < 10);
    System.out.println(s);
  }

  @Test
  public void upperCaseOf() {
    String s = RandomHelper.getUpperCase(5);
    assertEquals(5, s.length());
    System.out.println(s);
  }

  @Test
  public void upperCaseOf1() {
    String s = RandomHelper.getUpperCase(5, 10);
    assertTrue(s.length() >= 5 && s.length() < 10);
    System.out.println(s);
  }
}