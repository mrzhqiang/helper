package helper.sample;

import helper.RandomStrings;
import helper.javafx.Applications;
import helper.javafx.model.Status;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class RandomStringsSample extends Application {

  @FXML Label accountLabel;
  @FXML Label passwordLabel;
  @FXML Label tokenLabel;
  @FXML Label idLabel;
  @FXML Label usernameLabel;
  @FXML Label nicknameLabel;
  @FXML Label realNameLabel;
  @FXML Button generateAccountButton;
  @FXML Button generateUserButton;

  public static void main(String[] args) {
    launch(args);
  }

  private final Status status = new Status();

  private final CompositeDisposable disposable = new CompositeDisposable();

  @Override
  public void start(Stage primaryStage) {
    Applications.start(this, primaryStage);
  }

  @Override public void stop() throws Exception {
    super.stop();
    disposable.clear();
    Schedulers.shutdown();
  }

  @FXML void initialize() {
    disposable.add(status.observe()
        .subscribe(running -> {
          generateAccountButton.setDisable(running);
          generateUserButton.setDisable(running);
        }));
  }

  @FXML void onGenerateAccountClicked() {
    status.running();
    accountLabel.setText(RandomStrings.ofLength(10, 16));
    passwordLabel.setText(RandomStrings.ofNumber(6, 10));
    tokenLabel.setText(RandomStrings.ofLength(140));
    idLabel.setText(RandomStrings.ofNumber(10));
    status.finished();
  }

  @FXML void onGenerateUserClicked() {
    status.running();
    usernameLabel.setText(RandomStrings.ofNumber(12));
    nicknameLabel.setText(RandomStrings.ofChinese(5));
    realNameLabel.setText(RandomStrings.ofSurname() + RandomStrings.ofChinese(1, 2));
    status.finished();
  }
}
