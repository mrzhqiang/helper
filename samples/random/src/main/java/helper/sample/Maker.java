package helper.sample;

import helper.RandomHelper;
import javafx.application.Application;
import javafx.stage.Stage;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

/**
 * @author mrzhqiang
 */
public final class Maker extends Application {
  private JPanel panelBase;

  private JButton btnGenerateAccount;
  private JButton btnGenerateUser;
  private JLabel labelUsername;
  private JLabel labelPassword;
  private JTextArea labelToken;
  private JLabel labelId;
  private JLabel labelUserId;
  private JLabel labelNickname;
  private JLabel labelRealName;

  @Override public void init() throws Exception {
    super.init();
    btnGenerateAccount.addActionListener(e -> {
      labelUsername.setText(RandomHelper.getString(10, 16));
      labelPassword.setText(RandomHelper.getNumber(6, 10));
      labelToken.setText(RandomHelper.getString(140));
      labelId.setText(RandomHelper.getNumber(10));
    });
    btnGenerateUser.addActionListener(e -> {
      labelUserId.setText(RandomHelper.getNumber(12));
      labelNickname.setText(RandomHelper.getChinese(5));
      labelRealName.setText(RandomHelper.getSurname() + RandomHelper.getChinese(1, 2));
    });
  }

  @Override public void start(Stage primaryStage) {
    JFrame maker = new JFrame("Maker");
    maker.setContentPane(panelBase);
    maker.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    maker.pack();
    maker.setResizable(false);
    maker.setLocationRelativeTo(maker.getOwner());
    maker.setVisible(true);
  }

  @Override public void stop() throws Exception {
    super.stop();
  }

  public static void main(String[] args) {
    String feelClassName = UIManager.getSystemLookAndFeelClassName();
    try {
      UIManager.setLookAndFeel(feelClassName);
    } catch (Exception e) {
      e.printStackTrace();
    }
    launch(args);
  }
}
