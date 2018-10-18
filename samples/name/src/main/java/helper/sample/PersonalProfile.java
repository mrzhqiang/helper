package helper.sample;

import helper.NameHelper;
import helper.RandomHelper;
import java.awt.Color;
import javafx.application.Application;
import javafx.stage.Stage;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

/**
 * 个人资料 demo。
 *
 * @author mrzhqiang
 */
public final class PersonalProfile extends Application {
  private JPanel panelBase;
  private JLabel labelNickname;
  private JLabel labelRealName;
  private JRadioButton radioBtnNickname;
  private JRadioButton radioBtnRealName;
  private JSpinner spinnerNicknameMin;
  private JSpinner spinnerNicknameMax;
  private JSpinner spinnerRealNameMin;
  private JSpinner spinnerRealNameMax;
  private JButton btnGenerate;
  private JLabel labelColor;
  private JLabel labelFirst;
  private JPanel panelColor;

  @Override public void init() throws Exception {
    super.init();
    SpinnerNumberModel numberModel = new SpinnerNumberModel();
    numberModel.setStepSize(1);
    numberModel.setMaximum(10);
    numberModel.setValue(1);
    numberModel.setMinimum(1);
    spinnerNicknameMin.setModel(numberModel);
    numberModel = new SpinnerNumberModel();
    numberModel.setStepSize(1);
    numberModel.setMaximum(10);
    numberModel.setValue(10);
    numberModel.setMinimum(1);
    spinnerNicknameMax.setModel(numberModel);
    numberModel = new SpinnerNumberModel();
    numberModel.setStepSize(1);
    numberModel.setMaximum(2);
    numberModel.setValue(1);
    numberModel.setMinimum(1);
    spinnerRealNameMin.setModel(numberModel);
    numberModel = new SpinnerNumberModel();
    numberModel.setStepSize(1);
    numberModel.setMaximum(2);
    numberModel.setValue(2);
    numberModel.setMinimum(1);
    spinnerRealNameMax.setModel(numberModel);

    btnGenerate.addActionListener(e -> {
      if (radioBtnNickname.isSelected()) {
        int min = (int) spinnerNicknameMin.getValue();
        int max = (int) spinnerNicknameMax.getValue();
        String text = RandomHelper.getUpperCase(min, max);
        if (NameHelper.checkName(text, min, max)) {
          updateInfo(text, labelNickname);
          return;
        }
        labelNickname.setText("Error！");
      } else if (radioBtnRealName.isSelected()) {
        int min = (int) spinnerRealNameMin.getValue();
        int max = (int) spinnerRealNameMax.getValue();
        String text = RandomHelper.getSurname() + RandomHelper.getChinese(min, max);
        if (NameHelper.checkChinese(text, min - 1, max + 1)) {
          updateInfo(text, labelRealName);
          return;
        }
        labelRealName.setText("Error！");
      }
    });
  }

  private void updateInfo(String text, JLabel labelNickname) {
    labelNickname.setText(text);
    labelFirst.setText(NameHelper.firstLetter(text));
    String color = NameHelper.color(text);
    labelColor.setText(color);
    panelColor.setBackground(Color.decode(color));
  }

  @Override public void stop() throws Exception {
    super.stop();
  }

  @Override public void start(Stage primaryStage) {
    JFrame frame = new JFrame("Personal");
    frame.setContentPane(panelBase);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.pack();
    frame.setResizable(false);
    frame.setLocationRelativeTo(frame.getOwner());
    frame.setVisible(true);
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
