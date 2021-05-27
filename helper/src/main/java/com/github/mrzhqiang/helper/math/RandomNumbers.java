package com.github.mrzhqiang.helper.math;

import java.security.SecureRandom;
import java.util.Random;

public enum RandomNumbers {
    INSTANCE;

    private final Random random = new SecureRandom();

    public static int nextInt() {
        return INSTANCE.random.nextInt();
    }

    public static int nextInt(int bound) {
        return INSTANCE.random.nextInt(bound);
    }

    public static int rangeInt(int min, int max) {
        if (min > max) {
            min = max;
        }
        // Random.nextDouble == Math.random
        return (int) (min + nextDouble() * (max - min + 1));
    }

    public static long nextLong() {
        return INSTANCE.random.nextLong();
    }

    public static double nextDouble() {
        // return [0.0, 1.0)
        return INSTANCE.random.nextDouble();
    }

    public static float nextFloat() {
        return INSTANCE.random.nextFloat();
    }

    public static boolean nextBoolean() {
        return INSTANCE.random.nextBoolean();
    }
}
