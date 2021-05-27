package com.github.mrzhqiang.helper.sample;

import com.github.mrzhqiang.helper.time.Dates;
import com.github.mrzhqiang.helper.javafx.Applications;
import com.github.mrzhqiang.helper.javafx.model.Console;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class DatesSample extends Application {

    @FXML
    Label systemTimeLabel;
    @FXML
    Label startTimeLabel;
    @FXML
    Label durationLabel;
    @FXML
    TextArea consoleTextArea;

    public static void main(String[] args) {
        launch(args);
    }

    private Date startTime;
    private final Console console = new Console();

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public void start(Stage primaryStage) {
        Applications.start(this, primaryStage);
    }

    @Override
    public void stop() {
        disposable.clear();
        Schedulers.shutdown();
    }

    @FXML
    void initialize() {
        startTime = new Date();
        disposable.add(console.observe().subscribe(consoleTextArea::appendText));
        disposable.add(Observable.interval(1, 1, TimeUnit.SECONDS)
                .map(aLong -> Dates.format(new Date()))
                .observeOn(JavaFxScheduler.platform())
                .doOnNext(systemTime -> systemTimeLabel.setText(systemTime))
                .map(this::parseDate)
                .map(date -> String.format("解析启动：%s", date))
                .doOnNext(console::log)
                .map(s -> String.format("%s | %s",
                        Dates.formatDate(startTime), Dates.formatTime(startTime)))
                .doOnNext(s -> startTimeLabel.setText(s))
                .map(s -> Dates.untilNow(startTime))
                .doOnNext(s -> durationLabel.setText(s))
                .map(s -> startTime.toInstant().minus(1, ChronoUnit.DAYS))
                .map(instant ->
                        String.format("显示昨天：%s%s", Dates.display(Date.from(instant)), System.lineSeparator()))
                .subscribe(console::log));
    }

    private Date parseDate(String source) {
        Date parse = Dates.parse(source);
        return Objects.nonNull(parse) ? parse : new Date();
    }
}
