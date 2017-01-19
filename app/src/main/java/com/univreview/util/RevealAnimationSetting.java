package com.univreview.util;

/**
 * Created by DavidHa on 2017. 1. 19..
 */
public abstract class RevealAnimationSetting {
    public abstract int getCenterX();

    public abstract int getCenterY();

    public abstract int getWidth();

    public abstract int getHeight();

    public static RevealAnimationSetting with(int centerX, int centerY, int width, int height) {
        return new AutoValueRevealAnimationSetting(centerX, centerY, width, height);
    }
}
