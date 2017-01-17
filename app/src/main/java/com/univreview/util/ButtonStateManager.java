package com.univreview.util;

import android.graphics.drawable.Drawable;

import com.univreview.log.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DavidHa on 2017. 1. 17..
 */
public class ButtonStateManager {
   protected List<ButtonState> btnStates = new ArrayList<>();
   private int currentIndex = -1;
   private boolean flag = false;

   public ButtonStateManager(List<ButtonState> btnStates) {
      this.btnStates = btnStates;
   }

   public void clickButton(Integer index) {
      if (index != null) {
         Logger.v("click button");
         refresh();
         if (flag) {
            if (currentIndex != index) {
               btnStates.get(index).clickButton();
               currentIndex = index;
            } else {
               currentIndex = -1;
            }
         } else {
            btnStates.get(index).clickButton();
         }
      }
   }

   public void setTxtColor(int normalColor, int selectedColor) {
      for (ButtonState btnState : btnStates) {
         btnState.setTxtColor(normalColor, selectedColor);
      }
   }

   public void setDrawable(Drawable normalDrawable, Drawable selectedDrawable) {
      for (ButtonState btnState : btnStates) {
         btnState.setDrawable(normalDrawable, selectedDrawable);
      }
   }

   public void refresh(){
      for (ButtonState btnState : btnStates) {
         btnState.setButtonState(false);
      }
   }
}
