package com.contusfly.smoothprogressbar;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;

import com.polls.polls.R;

/**
 * Created by castorflex on 11/10/13.
 */
public class SmoothProgressBar extends ProgressBar {
//INTERPOLATOR_ACCELERATE
  private static final int INTERPOLATOR_ACCELERATE = 0;
// INTERPOLATOR_LINEAR
  private static final int INTERPOLATOR_LINEAR = 1;
  //INTERPOLATOR_ACCELERATEDECELERATE
  private static final int INTERPOLATOR_ACCELERATEDECELERATE = 2;
//INTERPOLATOR_DECELERATE
  private static final int INTERPOLATOR_DECELERATE = 3;

  /**
   * This class represents a constructor. Information about the constructor can be accessed,
   * and the constructor can be invoked dynamically.
   * @param context
   */
  public SmoothProgressBar(Context context) {
    this(context, null);
  }
  /**
   * This class represents a constructor. Information about the constructor can be accessed,
   * and the constructor can be invoked dynamically.
   * @param context
   */
  public SmoothProgressBar(Context context, AttributeSet attrs) {
    this(context, attrs, R.attr.spbStyle);
  }
  /**
   * This class represents a constructor. Information about the constructor can be accessed,
   * and the constructor can be invoked dynamically.
   * @param context
   */
  public SmoothProgressBar(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);

    if (isInEditMode()) {
      //Define the drawable used to draw the progress bar in indeterminate mode.
      setIndeterminateDrawable(new SmoothProgressDrawable.Builder(context, true).build());
      return;
    }
   //Class for accessing an application's resources. This sits on top of the asset manager of the application
   // (accessible through getAssets()) and provides a high-level API for getting typed data from the assets.
    Resources res = context.getResources();
    //Container for an array of values that were retrieved with obtainStyledAttributes(AttributeSet, int[], int, int)
    // or obtainAttributes(AttributeSet, int[]). Be sure to call recycle() when done with them.
    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SmoothProgressBar, defStyle, 0);
    //color
    final int color = a.getColor(R.styleable.SmoothProgressBar_spb_color, res.getColor(R.color.spb_default_color));
    //sectionsCount
    final int sectionsCount = a.getInteger(R.styleable.SmoothProgressBar_spb_sections_count, res.getInteger(R.integer.spb_default_sections_count));
   //separatorLength
    final int separatorLength = a.getDimensionPixelSize(R.styleable.SmoothProgressBar_spb_stroke_separator_length, res.getDimensionPixelSize(R.dimen.spb_default_stroke_separator_length));
    //strokeWidth
    final float strokeWidth = a.getDimension(R.styleable.SmoothProgressBar_spb_stroke_width, res.getDimension(R.dimen.spb_default_stroke_width));
    //speed
    final float speed = a.getFloat(R.styleable.SmoothProgressBar_spb_speed, Float.parseFloat(res.getString(R.string.spb_default_speed)));
   //speed
    final float speedProgressiveStart = a.getFloat(R.styleable.SmoothProgressBar_spb_progressiveStart_speed, speed);
    //speedProgressiveStop
    final float speedProgressiveStop = a.getFloat(R.styleable.SmoothProgressBar_spb_progressiveStop_speed, speed);
    //iInterpolator
    final int iInterpolator = a.getInteger(R.styleable.SmoothProgressBar_spb_interpolator, -1);
    //reversed
    final boolean reversed = a.getBoolean(R.styleable.SmoothProgressBar_spb_reversed, res.getBoolean(R.bool.spb_default_reversed));
   //mirrorMode
    final boolean mirrorMode = a.getBoolean(R.styleable.SmoothProgressBar_spb_mirror_mode, res.getBoolean(R.bool.spb_default_mirror_mode));
  //colorsId
    final int colorsId = a.getResourceId(R.styleable.SmoothProgressBar_spb_colors, 0);
  //progressiveStartActivated
    final boolean progressiveStartActivated = a.getBoolean(R.styleable.SmoothProgressBar_spb_progressiveStart_activated, res.getBoolean(R.bool.spb_default_progressiveStart_activated));
   //backgroundDrawable
    final Drawable backgroundDrawable = a.getDrawable(R.styleable.SmoothProgressBar_spb_background);
   //generateBackgroundWithColors
    final boolean generateBackgroundWithColors = a.getBoolean(R.styleable.SmoothProgressBar_spb_generate_background_with_colors, false);
   //gradients
    final boolean gradients = a.getBoolean(R.styleable.SmoothProgressBar_spb_gradients, false);
    a.recycle();

    //interpolator
    Interpolator interpolator = null;
    if (iInterpolator == -1) {
      //Get interpolator
      interpolator = getInterpolator();
    }
    if (interpolator == null) {
      switch (iInterpolator) {
        case INTERPOLATOR_ACCELERATEDECELERATE:
          //An interpolator where the rate of change starts and ends slowly but
          // accelerates through the middle.
          interpolator = new AccelerateDecelerateInterpolator();
          break;
        case INTERPOLATOR_DECELERATE:
          interpolator = new DecelerateInterpolator();
          break;
        case INTERPOLATOR_LINEAR:
          //An interpolator where the rate of change is constant
          interpolator = new LinearInterpolator();
          break;
        case INTERPOLATOR_ACCELERATE:
        default:
          //An interpolator where the rate of change starts out slowly and
          // and then accelerates.
          interpolator = new AccelerateInterpolator();
      }
    }

    int[] colors = null;
    //colors
    if (colorsId != 0) {
      //The getIntArray() method returns the int array value of the variable. If the value is not of type int, "java.lang.ClassCastException" exception will be thrown.
      // To avoid this exception, check the type of the data and call the correct method for that type.
      colors = res.getIntArray(colorsId);
    }
//Small library allowing you to make a smooth indeterminate progress bar.
// You can either user your progress bars and set this drawable or use directly the SmoothProgressBarView.
    SmoothProgressDrawable.Builder builder = new SmoothProgressDrawable.Builder(context)
        .speed(speed)
        .progressiveStartSpeed(speedProgressiveStart)
        .progressiveStopSpeed(speedProgressiveStop)
        .interpolator(interpolator)
        .sectionsCount(sectionsCount)
        .separatorLength(separatorLength)
        .strokeWidth(strokeWidth)
        .reversed(reversed)
        .mirrorMode(mirrorMode)
        .progressiveStart(progressiveStartActivated)
        .gradients(gradients);

    if (backgroundDrawable != null) {
      //A drawable resource is a general concept for a graphic that can be drawn to
      // the screen and which you can retrieve with APIs such as getDrawable(int)
      builder.backgroundDrawable(backgroundDrawable);
    }

    if (generateBackgroundWithColors) {
      //Gnereate backgound with colors
      builder.generateBackgroundUsingColors();
    }

    if (colors != null && colors.length > 0)
      builder.colors(colors);
    else
      builder.color(color);
//Small library allowing you to make a smooth indeterminate progress bar.
// You can either user your progress bars and set this drawable or use directly the SmoothProgressBarView.
    SmoothProgressDrawable d = builder.build();
    setIndeterminateDrawable(d);
  }


  @Override
  protected synchronized void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (isIndeterminate() && getIndeterminateDrawable() instanceof SmoothProgressDrawable &&
        !((SmoothProgressDrawable) getIndeterminateDrawable()).isRunning()) {
      //Drawing a defined shape using OpenGL ES 2.0 requires a significant amount of code
      getIndeterminateDrawable().draw(canvas);
    }
  }

  /**
   * checkIndeterminateDrawable
   * @return ret
   */
  private SmoothProgressDrawable checkIndeterminateDrawable() {
    //A Drawable is a general abstraction for "something that can be drawn." Most often you will deal with Drawable as the type of resource retrieved for drawing things to the screen; the Drawable class provides a generic API
    // for dealing with an underlying visual resource that may take a variety of forms.
    Drawable ret = getIndeterminateDrawable();
    if (ret == null || !(ret instanceof SmoothProgressDrawable))
      throw new RuntimeException("The drawable is not a SmoothProgressDrawable");
    return (SmoothProgressDrawable) ret;
  }

  @Override
  public void setInterpolator(Interpolator interpolator) {
    super.setInterpolator(interpolator);
    //A Drawable is a general abstraction for "something that can be drawn." Most often you will deal with Drawable as the type of resource retrieved for drawing things to the screen; the Drawable class provides a generic API
    // for dealing with an underlying visual resource that may take a variety of forms.
    Drawable ret = getIndeterminateDrawable();
    if (ret != null && (ret instanceof SmoothProgressDrawable))
      ((SmoothProgressDrawable) ret).setInterpolator(interpolator);
  }

  /**
   * progressiveStart
   */
  public void progressiveStart() {
    //start
    checkIndeterminateDrawable().progressiveStart();
  }

  /**
   * progressiveStop
   */
  public void progressiveStop() {
    //stop
    checkIndeterminateDrawable().progressiveStop();
  }
}
