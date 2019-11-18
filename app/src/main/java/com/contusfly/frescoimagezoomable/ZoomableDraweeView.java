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

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeView;

/**
 * DraweeView that has zoomable capabilities.
 * <p>
 * Once the image loads, pinch-to-zoom and translation gestures are enabled.
 *
 */
public class ZoomableDraweeView extends DraweeView<GenericDraweeHierarchy>
    implements ZoomableController.Listener {
   //class tag
  private static final Class<?> TAG = ZoomableDraweeView.class;
//float value
  private static final float HUGE_IMAGE_SCALE_FACTOR_THRESHOLD = 1.1f;
//RectF holds four float coordinates for a rectangle. The rectangle is represented by the coordinates of its 4 edges (left, top, right bottom). These fields can be accessed directly.
// Use width() and height() to retrieve the rectangle's width and height.
// Note: most methods do not check to see that the coordinates are sorted correctly (i.e. left <= right and top <= bottom).
  private final RectF mImageBounds = new RectF();
  //RectF holds four float coordinates for a rectangle. The rectangle is represented by the coordinates of its 4 edges (left, top, right bottom). These fields can be accessed directly.
// Use width() and height() to retrieve the rectangle's width and height.
// Note: most methods do not check to see that the coordinates are sorted correctly (i.e. left <= right and top <= bottom).
  private final RectF mViewBounds = new RectF();
//Interface definition for a callback to be invoked when the status bar changes visibility.
  private final ControllerListener mControllerListener = new BaseControllerListener<Object>() {
    @Override
    public void onFinalImageSet(
        String id,
        @Nullable Object imageInfo,
        @Nullable Animatable animatable) {
      //Zoomable set
      ZoomableDraweeView.this.onFinalImageSet();
    }

    @Override
    public void onRelease(String id) {
      ZoomableDraweeView.this.onRelease();
    }
  };
  //DraweeControllers connect to the image pipeline — or to any image loader — and take care of backend image manipulation.
  private DraweeController mHugeImageController;
 //You'll now need to animate from the normal sized view to the zoomed view when appropriate.
  private ZoomableController mZoomableController = DefaultZoomableController.newInstance();

  /**
   *initializes a new instance of the class.
   * @param context
   */
  public ZoomableDraweeView(Context context) {
    super(context);
    init();
  }

  /**
   * ZoomableDraweeView
   * @param context
   * @param attrs
   */
  public ZoomableDraweeView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  /**
   * ZoomableDraweeView
   * @param context
   * @param attrs
   * @param defStyle
   */
  public ZoomableDraweeView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  /**
   * Initialize the zoomcontroller
   */
  private void init() {
    mZoomableController.setListener(this);
  }

  @Override
  public void setController(@Nullable DraweeController controller) {
    setControllers(controller, null);
  }

  private void setControllersInternal(
      @Nullable DraweeController controller,
      @Nullable DraweeController hugeImageController) {
    removeControllerListener(getController());
    addControllerListener(controller);
    mHugeImageController = hugeImageController;
    super.setController(controller);
  }

    /**
     * Sets the controllers for the normal and huge image.
     *
     * <p> IMPORTANT: in order to avoid a flicker when switching to the huge image, the huge image
     * controller should have the normal-image-uri set as its low-res-uri.
     *
     * @param controller controller to be initially used
     * @param hugeImageController controller to be used after the client starts zooming-in
     */
  public void setControllers(
      @Nullable DraweeController controller,
      @Nullable DraweeController hugeImageController) {
    setControllersInternal(null, null);
    //setting the boolean false
    mZoomableController.setEnabled(false);
    setControllersInternal(controller, hugeImageController);
  }

    /**
     * maybeSetHugeImageController
     */
  private void maybeSetHugeImageController() {
      //If mHugeImageController is not null and below contition satisfies
    if (mHugeImageController != null &&
        mZoomableController.getScaleFactor() > HUGE_IMAGE_SCALE_FACTOR_THRESHOLD) {
      setControllersInternal(mHugeImageController, null);
    }
  }

    /**
     * removeControllerListener
     * @param controller
     */
  private void removeControllerListener(DraweeController controller) {
      //remove the listener
    if (controller instanceof AbstractDraweeController) {
      ((AbstractDraweeController) controller)
          .removeControllerListener(mControllerListener);
    }
  }

    /**
     * addControllerListener
     * @param controller
     */
  private void addControllerListener(DraweeController controller) {
    if (controller instanceof AbstractDraweeController) {
      ((AbstractDraweeController) controller)
          .addControllerListener(mControllerListener);
    }
  }

  @Override
  protected void onDraw(Canvas canvas) {
    //  The Canvas class holds the "draw" calls. To draw something, you need 4 basic components:
    // A Bitmap to hold the pixels, a Canvas to host the draw calls (writing into the bitmap), a drawing primitive
    // (e.g. Rect, Path, text, Bitmap), and a paint (to describe the colors and styles for the drawing).
    int saveCount = canvas.save();
    canvas.concat(mZoomableController.getTransform());
     //Called when the view should render its content.
    super.onDraw(canvas);
      //Efficient way to pop any calls to save() that happened after the save count reached saveCount.
    canvas.restoreToCount(saveCount);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
      //Called when a touch screen motion event occurs.
    if (mZoomableController.onTouchEvent(event)) {
        //eturn the scaling factor from the previous scale event to the current event.
      if (mZoomableController.getScaleFactor() > 1.0f) {
          //Called when a child does not want this parent and its ancestors to intercept
          // touch events with onInterceptTouchEvent(MotionEvent).
        getParent().requestDisallowInterceptTouchEvent(true);
      }
      return true;
    }
    return super.onTouchEvent(event);
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
      //updateZoomableControllerBounds
    updateZoomableControllerBounds();
  }

  private void onFinalImageSet() {
      //If not enabled
    if (!mZoomableController.isEnabled()) {
        //updateZoomableControllerBounds
      updateZoomableControllerBounds();
        //set enbled true
      mZoomableController.setEnabled(true);
    }
  }

    /**
     * onRelease
     */
  private void onRelease() {
    mZoomableController.setEnabled(false);
  }

  @Override
  public void onTransformChanged(Matrix transform) {
    maybeSetHugeImageController();
// Mark the area defined by dirty as needing to be drawn.
    invalidate();
  }

    /**
     * updateZoomableControllerBounds
     */
  private void updateZoomableControllerBounds() {
    getHierarchy().getActualImageBounds(mImageBounds);
      //Setting the width and height
    mViewBounds.set(0, 0, getWidth(), getHeight());
    mZoomableController.setImageBounds(mImageBounds);
    mZoomableController.setViewBounds(mViewBounds);
  }
}
