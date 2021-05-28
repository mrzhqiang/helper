package com.github.mrzhqiang.helper.random;

import java.util.concurrent.ThreadLocalRandom;

/**
 * The docs copy from {@link ThreadLocalRandom}.
 * <p>
 * When applicable, use of ThreadLocalRandom rather than shared Random objects in
 * concurrent programs will typically encounter much less overhead and contention.
 * Use of ThreadLocalRandom is particularly appropriate when multiple tasks
 * (for example, each a ForkJoinTask) use random numbers in parallel in thread pools.
 */
public enum RandomNumbers {
    ;

    /**
     * Next random integer value.
     *
     * @return maybe any integer
     */
    public static int nextInt() {
        return ThreadLocalRandom.current().nextInt();
    }

    /**
     * Next random integer value from 0 to bound - 1.
     *
     * @param bound the upper bound (exclusive). Must be positive.
     * @return [0, bound)
     */
    public static int nextInt(int bound) {
        return ThreadLocalRandom.current().nextInt(bound);
    }

    /**
     * Next random integer value from min to max.
     *
     * @param min min value.
     * @param max max value.
     * @return [min, max]
     */
    public static int rangeInt(int min, int max) {
        if (min > max) {
            min = max;
        }
        // just [origin, bound)
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    /**
     * Next random long value.
     *
     * @return maybe any long
     */
    public static long nextLong() {
        return ThreadLocalRandom.current().nextLong();
    }

    /**
     * Next random long value from 0 to bound - 1.
     *
     * @param bound the upper bound (exclusive). Must be positive.
     * @return [0, bound)
     */
    public static long nextLong(long bound) {
        return ThreadLocalRandom.current().nextLong(bound);
    }

    /**
     * Next random long value from min to max.
     *
     * @param min min value.
     * @param max max value.
     * @return [min, max]
     */
    public static long rangeLong(long min, long max) {
        if (min > max) {
            min = max;
        }
        // just [origin, bound)
        return ThreadLocalRandom.current().nextLong(min, max + 1);
    }

    /**
     * Next random double value.
     *
     * @return [0.0, 1.0)
     */
    public static double nextDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }

    /**
     * Next random double value from 0 to bound (exclusive).
     *
     * @param bound the upper bound (exclusive). Must be positive.
     * @return [0, bound)
     */
    public static double nextDouble(double bound) {
        return ThreadLocalRandom.current().nextDouble(bound);
    }

    /**
     * Next random float value.
     *
     * @return [0.0, 1.0)
     */
    public static float nextFloat() {
        return ThreadLocalRandom.current().nextFloat();
    }

    /**
     * Returns a pseudorandom {@code boolean} value.
     *
     * @return a pseudorandom {@code boolean} value
     */
    public static boolean nextBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }
}
