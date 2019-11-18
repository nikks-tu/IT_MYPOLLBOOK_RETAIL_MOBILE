package com.contusfly.smoothprogressbar;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;

/**
 * Created by castorflex on 3/5/14.
 */
public final class SmoothProgressBarUtils {
  /**
   * Empty constructor
   */
  private SmoothProgressBarUtils() {
  }

  /**
   * generateDrawableWithColors
   * @param colors
   * @param strokeWidth
   * @return
   */
  public static Drawable generateDrawableWithColors(int[] colors, float strokeWidth) {
    if (colors == null || colors.length == 0) return null;
//A Drawable object that draws primitive shapes. A ShapeDrawable takes a Shape object and manages its presence on the screen.
// If no Shape is given, then the ShapeDrawable will default to a RectShape.
    return new ShapeDrawable(new ColorsShape(strokeWidth, colors));
  }

  /**
   * checkSpeed
   * @param speed speed
   */
  static void checkSpeed(float speed) {
    if (speed <= 0f)
      //Thrown when a method is invoked with an argument which it can not reasonably deal with.
      throw new IllegalArgumentException("Speed must be >= 0");
  }

  /**
   * checkColors
   * @param colors
   */
  static void checkColors(int[] colors) {
    if (colors == null || colors.length == 0)
      //Thrown when a method is invoked with an argument which it can not reasonably deal with.
      throw new IllegalArgumentException("You must provide at least 1 color");
  }

  /**
   * checkPositiveOrZero
   * @param number
   * @param name
   */
  static void checkPositiveOrZero(float number, String name) {
    if (number < 0)
      //Thrown when a method is invoked with an argument which it can not reasonably deal with.
      throw new IllegalArgumentException(String.format("%s %d must be positive", name, number));
  }

  /**
   * checkPositive
   * @param number
   * @param name
   */
  static void checkPositive(int number, String name){
    if(number <= 0)
// Thrown when a method is invoked with an argument which it can not reasonably deal with.
      throw new IllegalArgumentException(String.format("%s must not be null", name));
  }

  /**
   * checkNotNull
   * @param o
   * @param name
   */
  static void checkNotNull(Object o, String name) {
    if (o == null)
      //Thrown when a method is invoked with an argument which it can not reasonably deal with.
      throw new IllegalArgumentException(String.format("%s must be not null", name));
  }
}
