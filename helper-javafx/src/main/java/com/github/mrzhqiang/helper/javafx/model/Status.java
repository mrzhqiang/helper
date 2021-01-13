package com.github.mrzhqiang.helper.javafx.model;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * @see javafx.scene.control.Button
 * @see javafx.scene.control.TextField
 */
public class Status {

    private final BooleanProperty status = new SimpleBooleanProperty(false);

    public Observable<Boolean> observe() {
        return JavaFxObservable.valuesOf(status).observeOn(JavaFxScheduler.platform());
    }

    public void running() {
        status.setValue(Boolean.TRUE);
    }

    public void finished() {
        status.setValue(Boolean.FALSE);
    }
}
