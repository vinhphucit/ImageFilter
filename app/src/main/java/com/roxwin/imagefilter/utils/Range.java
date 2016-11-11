package com.roxwin.imagefilter.utils;

/**
 * Created by PhucTV on 5/9/16.
 */
public class Range {
    public static float range(final int percentage, final float start, final float end) {
        return (end - start) * percentage / 100.0f + start;
    }

    public static int range(final int percentage, final int start, final int end) {
        return (end - start) * percentage / 100 + start;
    }
}
