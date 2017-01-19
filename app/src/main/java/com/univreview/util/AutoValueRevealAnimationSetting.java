package com.univreview.util;

/**
 * Created by DavidHa on 2017. 1. 19..
 */
public class AutoValueRevealAnimationSetting extends RevealAnimationSetting {
    private int centerX;
    private int centerY;
    private int width;
    private int height;

    public AutoValueRevealAnimationSetting(int centerX, int centerY, int width, int height) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.width = width;
        this.height = height;
    }

    @Override
    public int getCenterX() {
        return centerX;
    }

    @Override
    public int getCenterY() {
        return centerY;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }
}
