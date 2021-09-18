package com.github.mrzhqiang.helper.sample;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FileEncodingSample extends Application {

    @FXML
    TextField targetFileTextField;
    @FXML
    Button chooseFileButton;
    @FXML
    TableView fileListTableView;
    @FXML
    Label messageTextField;
    @FXML
    Button startButton;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    @FXML
    void initialize() {

    }

    @FXML
    void onChooseFileAction() {

    }

    @FXML
    void onStartAction() {

    }
}
