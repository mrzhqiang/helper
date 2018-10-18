package helper.datetime;

import helper.DateTimeHelper;
import java.util.Date;
import javafx.application.Application;
import javafx.stage.Stage;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

/**
 * 聊天室 demo。
 *
 * @author mrzhqiang
 */
public final class ChatRoom extends Application {
  private JPanel panelContent;
  private JTextArea chatHistory;
  private JLabel labelSystemTime;
  private JLabel labelParse;
  private JLabel labelUntilNow;

  private Date startTime;
  private Timer timer;

  public static void main(String[] args) {
    String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
    try {
      UIManager.setLookAndFeel(lookAndFeel);
    } catch (Exception e) {
      e.printStackTrace();
    }
    launch(args);
  }

  @Override public void init() {
    startTime = new Date();
    timer = new Timer(1000, e -> {
      labelSystemTime.setText(DateTimeHelper.format(new Date()));
      Date parse = DateTimeHelper.parse(labelSystemTime.getText());
      if (parse != null) {
        labelParse.setText(parse.toString());
      }
      labelUntilNow.setText(DateTimeHelper.untilNow(startTime));
      String content = String.format("startTime：%s %s", DateTimeHelper.formatDate(startTime),
          DateTimeHelper.formatTime(startTime));
      Message message = Message.ofText(content);
      chatHistory.append(message.toString());
      chatHistory.append("\n");
    });
    timer.start();
  }

  @Override public void start(Stage primaryStage) {
    JFrame frame = new JFrame("ChatRoom");
    frame.setContentPane(panelContent);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.pack();
    frame.setResizable(false);
    frame.setLocationRelativeTo(frame.getOwner());
    frame.setVisible(true);
  }

  @Override public void stop() throws Exception {
    super.stop();
    timer.stop();
  }
}
