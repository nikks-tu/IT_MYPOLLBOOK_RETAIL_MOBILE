package com.contusfly.smoothprogressbar;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.shapes.Shape;

/**
 * Created by castorflex on 3/5/14.
 */
public class ColorsShape extends Shape {
//Stroke width
  private float mStrokeWidth;
  //Colors
  private int[] mColors;

  /**
   * ColorsShape
   * @param strokeWidth strokeWidth
   * @param colors colors
   */
  public ColorsShape(float strokeWidth, int[] colors) {
    mStrokeWidth = strokeWidth;
    mColors = colors;
  }

  @Override
  public void draw(Canvas canvas, Paint paint) {
      //ratio
    float ratio = 1f / mColors.length;
    int i = 0;
      //The stroke width is defined in pixels
    paint.setStrokeWidth(mStrokeWidth);
    for (int color : mColors) {
        //Set the paint's color.
      paint.setColor(color);
      //Draw a line segment with the specified start and stop x,y coordinates, using the specified paint.
      canvas.drawLine(i * ratio * getWidth(), getHeight() / 2, ++i * ratio * getWidth(), getHeight() / 2, paint);
    }
  }
}
