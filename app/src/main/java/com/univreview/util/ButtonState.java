package com.univreview.util;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.univreview.log.Logger;

/**
 * Created by DavidHa on 2017. 1. 17..
 */
public abstract class ButtonState {
    protected boolean isSelected = false;
    protected Drawable normalDrawable;
    protected Drawable selectedDrawable;
    protected int normalTxtColor;
    protected int selectedTxtColor;
    protected Context context;

    public ButtonState() {
    }

    public ButtonState(Context context) {
        this.context = context;
    }

    public abstract void setButtonState(boolean isSelected);

    public void setDrawable(int normalId, int selectedId) {
        if (context != null) {
            this.normalDrawable = Util.getDrawable(context, normalId);
            this.selectedDrawable = Util.getDrawable(context, selectedId);
        } else {
            Logger.e("context is null");
        }
    }

    public ButtonState setTxtColor(int normalId, int selectedId) {
        if (context != null) {
            this.normalTxtColor = Util.getColor(context, normalId);
            this.selectedTxtColor = Util.getColor(context, selectedId);
        } else {
            Logger.e("context is null");
        }
        return this;
    }

    public void setContext(Context context) {
        this.context = context;
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
