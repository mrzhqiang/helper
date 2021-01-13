package com.github.mrzhqiang.helper.javafx.model;

import io.reactivex.observers.DefaultObserver;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import javax.annotation.Nonnull;

import static org.junit.Assert.assertEquals;

public class ProgressTest extends ApplicationTest {

    private final Progress progress = new Progress();
    private boolean first = true;

    @Test
    public void update() {
        progress.observe().subscribe(new DefaultObserver<Double>() {
            @Override
            public void onNext(@Nonnull Double aDouble) {
                if (first) {
                    first = false;
                    return;
                }
                assertEquals(Double.valueOf(0.5), aDouble);
            }

            @Override
            public void onError(@Nonnull Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });
        progress.update(0.5);
    }

    @Test
    public void compute() {
        progress.observe().subscribe(new DefaultObserver<Double>() {
            @Override
            public void onNext(@Nonnull Double aDouble) {
                if (first) {
                    first = false;
                    return;
                }
                assertEquals(Double.valueOf(0.1), aDouble);
            }

            @Override
            public void onError(@Nonnull Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });
        progress.compute(1, 10);
    }
}