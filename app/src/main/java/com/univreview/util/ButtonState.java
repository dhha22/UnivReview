package com.univreview.util;

import android.graphics.drawable.Drawable;

/**
 * Created by DavidHa on 2017. 1. 17..
 */
public abstract class ButtonState {
    protected boolean isSelected = false;
    protected Drawable normalDrawable;
    protected Drawable selectedDrawable;
    protected int normalTxtColor;
    protected int selectedTxtColor;

    public abstract void setButtonState(boolean isSelected);

    public void setDrawable(Drawable normalDrawable, Drawable selectedDrawable) {
        this.normalDrawable = normalDrawable;
        this.selectedDrawable = selectedDrawable;
    }

    public ButtonState setTxtColor(int normalTxtColor, int selectedTxtColor) {
        this.normalTxtColor = normalTxtColor;
        this.selectedTxtColor = selectedTxtColor;
        return this;
    }

    public void clickButton() {
        if (isSelected) {
            setButtonState(false);
        } else {
            setButtonState(true);
        }
    }

    public boolean getButtonState() {
        if (isSelected) {
            return true;
        }
        return false;
    }

}
