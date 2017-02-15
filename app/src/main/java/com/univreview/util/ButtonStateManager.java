package com.univreview.util;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.univreview.log.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by DavidHa on 2017. 1. 17..
 */
public class ButtonStateManager {
   protected List<ButtonState> btnStates = new ArrayList<>();
   private int currentIndex = -1;
   private boolean flag = false;

   public ButtonStateManager(Context context, ButtonState... buttonStates) {
      for (ButtonState buttonState : buttonStates) {
         buttonState.setContext(context);
         btnStates.add(buttonState);
      }
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

   public void setTxtColor(int normalId, int selectedId) {
      for (ButtonState btnState : btnStates) {
         btnState.setTxtColor(normalId, selectedId);
      }
   }

   public void setDrawable(int normalId, int selectedId) {
      for (ButtonState btnState : btnStates) {
         btnState.setDrawable(normalId, selectedId);
      }
   }

   public void refresh(){
      for (ButtonState btnState : btnStates) {
         btnState.setButtonState(false);
      }
   }
}
