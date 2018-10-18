package helper.sample;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.HeadlessException;

/**
 * @author mrzhqiang
 */
public class CircleAvatar extends Component {
  private static final long serialVersionUID = 4288285958489652032L;

  public String showTitle;
  public Color color;

  public CircleAvatar() throws HeadlessException {
    setSize(140, 140);
  }

  @Override public void paint(Graphics g) {
    super.paint(g);
    g.setColor(color);
    g.fillOval(getWidth() / 2, getHeight() / 2, getWidth(), getHeight());
    Color brighter = color.brighter();
    g.setColor(brighter);
    g.drawString(showTitle, getWidth() / 2, getHeight() / 2);
  }
}
