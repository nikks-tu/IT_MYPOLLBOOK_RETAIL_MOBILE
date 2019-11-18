/*
 * This file provided by Facebook is for non-commercial testing and evaluation
 * purposes only.  Facebook reserves all rights not expressly granted.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * FACEBOOK BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.contusfly.frescoimagezoomable;

import android.graphics.Matrix;
import android.graphics.RectF;
import android.view.MotionEvent;


/**
 * Zoomable controller that calculates transformation based on touch events.
 */
public class DefaultZoomableController
    implements ZoomableController, TransformGestureDetector.Listener {
///Component that detects translation, scale and rotation based on touch events.
  private TransformGestureDetector mGestureDetector;
//Listner
  private Listener mListener = null;
//Set the boolean
  private boolean mIsEnabled = false;
  //Set the boolean
  private boolean mIsRotationEnabled = false;
  //Set the boolean
  private boolean mIsScaleEnabled = true;
  //Set the boolean
  private boolean mIsTranslationEnabled = true;
//Float value
  private float mMinScaleFactor = 1.0f;
  //RectF holds four float coordinates for a rectangle. The rectangle is represented by the coordinates of its 4 edges (left, top, right bottom). These fields can be accessed directly.
// Use width() and height() to retrieve the rectangle's width and height.
// Note: most methods do not check to see that the coordinates are sorted correctly (i.e. left <= right and top <= bottom).
  private final RectF mViewBounds = new RectF();
  //RectF holds four float coordinates for a rectangle. The rectangle is represented by the coordinates of its 4 edges (left, top, right bottom). These fields can be accessed directly.
// Use width() and height() to retrieve the rectangle's width and height.
// Note: most methods do not check to see that the coordinates are sorted correctly (i.e. left <= right and top <= bottom).
  private final RectF mImageBounds = new RectF();
  //RectF holds four float coordinates for a rectangle. The rectangle is represented by the coordinates of its 4 edges (left, top, right bottom). These fields can be accessed directly.
// Use width() and height() to retrieve the rectangle's width and height.
// Note: most methods do not check to see that the coordinates are sorted correctly (i.e. left <= right and top <= bottom).
  private final RectF mTransformedImageBounds = new RectF();
// The Matrix class holds a 3x3 matrix for transforming coordinates.
  private final Matrix mPreviousTransform = new Matrix();
  //The Matrix class holds a 3x3 matrix for transforming coordinates.
  private final Matrix mActiveTransform = new Matrix();
// The Matrix class holds a 3x3 matrix for transforming coordinates.
  private final Matrix mActiveTransformInverse = new Matrix();
  //Float
  private final float[] mTempValues = new float[9];

  /**
   * DefaultZoomableController
   * @param gestureDetector
   */
  public DefaultZoomableController(TransformGestureDetector gestureDetector) {
    mGestureDetector = gestureDetector;
    mGestureDetector.setListener(this);
  }

  /**
   *Create a new reader for images of the desired size and format.
   * @return DefaultZoomableController(TransformGestureDetector.newInstance())
   */
  public static DefaultZoomableController newInstance() {
    return new DefaultZoomableController(TransformGestureDetector.newInstance());
  }

  @Override
  public void setListener(Listener listener) {
    mListener = listener;
  }

  /** Rests the controller. */
  public void reset() {
    //reset
    mGestureDetector.reset();
    //reset
    mPreviousTransform.reset();
    //reset
    mActiveTransform.reset();
  }

  /** Sets whether the controller is enabled or not. */
  @Override
  public void setEnabled(boolean enabled) {
    mIsEnabled = enabled;
    if (!enabled) {
      //reset
      reset();
    }
  }

  /** Returns whether the controller is enabled or not. */
  @Override
  public boolean isEnabled() {
    return mIsEnabled;
  }


  /** Sets the image bounds before zoomable transformation is applied. */
  @Override
  public void setImageBounds(RectF imageBounds) {
    mImageBounds.set(imageBounds);
  }

  /** Sets the view bounds. */
  @Override
  public void setViewBounds(RectF viewBounds) {
    mViewBounds.set(viewBounds);
  }



  /**
   * Gets the zoomable transformation
   * Internal matrix is exposed for performance reasons and is not to be modified by the callers.
   */
  @Override
  public Matrix getTransform() {
    return mActiveTransform;
  }

  /** Notifies controller of the received touch event.  */
  @Override
  public boolean onTouchEvent(MotionEvent event) {
    //If is enabled
    if (mIsEnabled) {
      //Touch event
      return mGestureDetector.onTouchEvent(event);
    }
    return false;
  }

  /* TransformGestureDetector.Listener methods  */

  @Override
  public void onGestureBegin(TransformGestureDetector detector) {
  }

  @Override
  public void onGestureUpdate(TransformGestureDetector detector) {
    //setting the value
    mActiveTransform.set(mPreviousTransform);
    //true
    if (mIsRotationEnabled) {
      //float angle
      float angle = detector.getRotation() * (float) (180 / Math.PI);
    //   Postconcats the matrix with the specified scale.
      mActiveTransform.postRotate(angle, detector.getPivotX(), detector.getPivotY());
    }
    //true
    if (mIsScaleEnabled) {
      //float angle
      float scale = detector.getScale();
      //Postconcats the matrix with the specified scale.
      mActiveTransform.postScale(scale, scale, detector.getPivotX(), detector.getPivotY());
    }
    limitScale(detector.getPivotX(), detector.getPivotY());
    if (mIsTranslationEnabled) {
      //Postconcats the matrix with the specified translation. M' = T(dx, dy) * M
      mActiveTransform.postTranslate(detector.getTranslationX(), detector.getTranslationY());
    }
    limitTranslation();
    if (mListener != null) {
      mListener.onTransformChanged(mActiveTransform);
    }
  }

  @Override
  public void onGestureEnd(TransformGestureDetector detector) {
    mPreviousTransform.set(mActiveTransform);
  }

  /** Gets the current scale factor. */
  @Override
  public float getScaleFactor() {
    mActiveTransform.getValues(mTempValues);
    return mTempValues[Matrix.MSCALE_X];
  }

  /**
   * limitScale
   * @param pivotX
   * @param pivotY
   */
  private void limitScale(float pivotX, float pivotY) {
    //current scale
    float currentScale = getScaleFactor();
    if (currentScale < mMinScaleFactor) {
      //scale
      float scale = mMinScaleFactor / currentScale;
      mActiveTransform.postScale(scale, scale, pivotX, pivotY);
    }
  }

  private void limitTranslation() {
    //RectF holds four float coordinates for a rectangle. The rectangle is represented by the coordinates of its 4 edges (left, top, right bottom). These fields can be accessed directly.
    RectF bounds = mTransformedImageBounds;
    //(deep) copy the src matrix into this matrix.
    bounds.set(mImageBounds);
    //Apply this matrix to the src rectangle, and write the transformed rectangle into dst.
    mActiveTransform.mapRect(bounds);
    //offsetLeft
    float offsetLeft = getOffset(bounds.left, bounds.width(), mViewBounds.width());
    //offsetTop
    float offsetTop = getOffset(bounds.top, bounds.height(), mViewBounds.height());
    if (Float.floatToIntBits(offsetLeft) != Float.floatToIntBits(bounds.left) || Float.floatToIntBits(offsetTop) != Float.floatToIntBits(bounds.top)) {
      mActiveTransform.postTranslate(offsetLeft - bounds.left, offsetTop - bounds.top);
      //Restart gesture
      mGestureDetector.restartGesture();
    }
  }

  /**
   * getOffset
   * @param offset
   * @param imageDimension
   * @param viewDimension
   * @return
   */
  private float getOffset(float offset, float imageDimension, float viewDimension) {
    //float value diff
    float diff = viewDimension - imageDimension;
    return (diff > 0) ? diff / 2 : limit(offset, diff, 0);
  }

  /**
   * Calculating the PAGE_LIMIT
   * @param value
   * @param min
   * @param max
   * @return
   */
  private float limit(float value, float min, float max) {
    return Math.min(Math.max(min, value), max);
  }

}
