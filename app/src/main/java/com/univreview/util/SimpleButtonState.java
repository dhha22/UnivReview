package com.univreview.util;

import android.widget.Button;

import com.univreview.log.Logger;

/**
 * Created by DavidHa on 2017. 1. 17..
 */
public class SimpleButtonState extends ButtonState {
    private Button button;

    public SimpleButtonState(Button button) {
        this.button = button;
    }

    @Override
    public void setButtonState(boolean isSelected) {
        if (selectedTxtColor != 0 && normalTxtColor != 0
                && normalDrawable != null && selectedDrawable != null) {
            if (isSelected) {
                Logger.v(button.getText().toString() + " selected");
                button.setBackground(selectedDrawable);
                button.setTextColor(selectedTxtColor);
            } else {
                button.setBackground(normalDrawable);
                button.setTextColor(normalTxtColor);
            }
        }
        this.isSelected = isSelected;
    }

}
