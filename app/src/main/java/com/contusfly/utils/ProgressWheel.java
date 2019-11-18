package com.contusfly.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.polls.polls.R;

/**
 * A Material style progress wheel, compatible up to 2.2. Todd Davies' Progress
 * Wheel https://github.com/Todd-Davies/ProgressWheel
 * 
 * @author <p/>
 *         Licensed under the Apache License 2.0 license see:
 *         http://www.apache.org/licenses/LICENSE-2.0
 */
public class ProgressWheel extends View {

    /** Sizes (with defaults in DP)*. */
    private int circleRadius = 28;

    /** The bar width. */
    private int barWidth = 4;

    /** The rim width. */
    private int rimWidth = 4;

    /** The Constant BARLENGTH. */
    private static final int BARLENGTH = 16;

    /** The Constant BAR_MAX_LENGTH. */
    private static final int BAR_MAX_LENGTH = 270;

    /** The fill radius. */
    private boolean fillRadius = false;

    /** The time start growing. */
    private double timeStartGrowing = 0;

    /** The bar spin cycle time. */
    private double barSpinCycleTime = 460;

    /** The bar extra length. */
    private float barExtraLength = 0;

    /** The bar growing from front. */
    private boolean barGrowingFromFront = true;

    /** The paused time without growing. */
    private long pausedTimeWithoutGrowing = 0;

    /** The Constant PAUSE_GROWING_TIME. */
    private final static long PAUSE_GROWING_TIME = 200;

    /** The bar color. */
    private int barColor = 0xAA000000;

    /** The rim color. */
    private int rimColor = 0x00FFFFFF;

    /** The bar paint. */
    private Paint barPaint = new Paint();

    /** The rim paint. */
    private Paint rimPaint = new Paint();
    // Rectangles
    /** The circle bounds. */
    private RectF circleBounds = new RectF();

    /** The spin speed. */
    private float spinSpeed = 230.0f;

    /** The last time animated. */
    private long lastTimeAnimated = 0;

    /** The linear progress. */
    private boolean linearProgress;

    /** The m progress. */
    private float mProgress = 0.0f;

    /** The m target progress. */
    private float mTargetProgress = 0.0f;

    /** The is spinning. */
    private boolean isSpinning = false;

    /** The callback. */
    private ProgressCallback callback;

    /**
     * The constructor for the ProgressWheel.
     * 
     * @param context
     *            the context
     * @param attrs
     *            the attrs
     */
    public ProgressWheel(Context context, AttributeSet attrs) {
        super(context, attrs);
        /** styleble **/
        parseAttributes(context.obtainStyledAttributes(attrs,
                R.styleable.ProgressWheel));
    }

    /**
     * The constructor for the ProgressWheel.
     * 
     * @param context
     *            the context
     */
    public ProgressWheel(Context context) {
        super(context);
    }

    // ----------------------------------
    // Setting up stuff
    // ----------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see android.view.View#onMeasure(int, int)
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /** View width **/
        int viewWidth = circleRadius + this.getPaddingLeft()
                + this.getPaddingRight();
        /** View height **/
        int viewHeight = circleRadius + this.getPaddingTop()
                + this.getPaddingBottom();
        /** View width mode **/
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        /** View width size **/
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        /** View height mode **/
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        /** View height size **/
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        /** int width **/
        int width;
        /** int height **/
        int height;

        // Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            // Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            // Can't be bigger than...
            width = Math.min(viewWidth, widthSize);
        } else {
            // Be whatever you want
            width = viewWidth;
        }

        // Measure Height
        if (heightMode == MeasureSpec.EXACTLY
                || widthMode == MeasureSpec.EXACTLY) {
            // Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            // Can't be bigger than...
            height = Math.min(viewHeight, heightSize);
        } else {
            // Be whatever you want
            height = viewHeight;
        }

        setMeasuredDimension(width, height);
    }

    /**
     * Use onSizeChanged instead of onAttachedToWindow to get the dimensions of
     * the view, because this method is called after measuring the dimensions of
     * MATCH_PARENT & WRAP_CONTENT. Use this dimensions to setup the bounds and
     * paints.
     * 
     * @param w
     *            the w
     * @param h
     *            the h
     * @param oldw
     *            the oldw
     * @param oldh
     *            the oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        /** Set the bounds **/
        setupBounds(w, h);
        setupPaints();
        invalidate();
    }

    /**
     * Set the properties of the paints we're using to draw the progress wheel.
     */
    private void setupPaints() {
        /** Setting bar color **/
        barPaint.setColor(barColor);
        /** Setting anti alias **/
        barPaint.setAntiAlias(true);
        /** Setting stroke **/
        barPaint.setStyle(Style.STROKE);
        /** Setting bar width **/
        barPaint.setStrokeWidth(barWidth);
        /** Setting the color* */
        rimPaint.setColor(rimColor);
        rimPaint.setAntiAlias(true);
        /** Setting the stroke **/
        rimPaint.setStyle(Style.STROKE);
        /** Setting the stroke width **/
        rimPaint.setStrokeWidth(rimWidth);
    }

    /**
     * Set the bounds of the component.
     * 
     * @param layoutWidth
     *            the layout width
     * @param layoutHeight
     *            the layout height
     */
    private void setupBounds(int layoutWidth, int layoutHeight) {
        /** get padding top **/
        int paddingTop = getPaddingTop();
        /** Getting padding bottonm **/
        int paddingBottom = getPaddingBottom();
        /** Getting padding left **/
        int paddingLeft = getPaddingLeft();
        /** Getting padding right **/
        int paddingRight = getPaddingRight();

        if (!fillRadius) {
            // Width should equal to Height, find the min value to setup the
            // circle
            int minValue = Math.min(layoutWidth - paddingLeft - paddingRight,
                    layoutHeight - paddingBottom - paddingTop);

            int circleDiameter = Math.min(minValue, circleRadius * 2 - barWidth
                    * 2);

            // Calc the Offset if needed for centering the wheel in the
            // available space
            int xOffset = (layoutWidth - paddingLeft - paddingRight - circleDiameter)
                    / 2 + paddingLeft;
            int yOffset = (layoutHeight - paddingTop - paddingBottom - circleDiameter)
                    / 2 + paddingTop;

            circleBounds = new RectF(xOffset + barWidth, yOffset + barWidth,
                    xOffset + circleDiameter - barWidth, yOffset
                            + circleDiameter - barWidth);
        } else {
            circleBounds = new RectF(paddingLeft + barWidth, paddingTop
                    + barWidth, layoutWidth - paddingRight - barWidth,
                    layoutHeight - paddingBottom - barWidth);
        }
    }

    /**
     * Parse the attributes passed to the view from the XML.
     * 
     * @param a
     *            the attributes to parse
     */
    private void parseAttributes(TypedArray a) {
        // We transform the default values from DIP to pixels
        DisplayMetrics metrics = getContext().getResources()
                .getDisplayMetrics();
        barWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                barWidth, metrics);
        rimWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                rimWidth, metrics);
        circleRadius = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, circleRadius, metrics);

        circleRadius = (int) a.getDimension(
                R.styleable.ProgressWheel_matProg_circleRadius, circleRadius);

        fillRadius = a.getBoolean(R.styleable.ProgressWheel_matProg_fillRadius,
                false);

        barWidth = (int) a.getDimension(
                R.styleable.ProgressWheel_matProg_barWidth, barWidth);

        rimWidth = (int) a.getDimension(
                R.styleable.ProgressWheel_matProg_rimWidth, rimWidth);

        float baseSpinSpeed = a
                .getFloat(R.styleable.ProgressWheel_matProg_spinSpeed,
                        spinSpeed / 360.0f);
        /** Will calculatate tye spin speed **/
        spinSpeed = baseSpinSpeed * 360;

        barSpinCycleTime = a.getInt(
                R.styleable.ProgressWheel_matProg_barSpinCycleTime,
                (int) barSpinCycleTime);

        barColor = a.getColor(R.styleable.ProgressWheel_matProg_barColor,
                barColor);

        rimColor = a.getColor(R.styleable.ProgressWheel_matProg_rimColor,
                rimColor);

        linearProgress = a.getBoolean(
                R.styleable.ProgressWheel_matProg_linearProgress, false);

        if (a.getBoolean(
                R.styleable.ProgressWheel_matProg_progressIndeterminate, false)) {
            spin();
        }

        // Recycle
        a.recycle();
    }

    /**
     * Sets the callback.
     * 
     * @param progressCallback
     *            the new callback
     */
    public void setCallback(ProgressCallback progressCallback) {
        callback = progressCallback;
        /* If it is not spinning will call the run call back method* */
        if (!isSpinning) {
            runCallback();
        }
    }

    // ----------------------------------
    // Animation stuff
    // ----------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see android.view.View#onDraw(android.graphics.Canvas)
     */
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /** Draw a arc of circle bbounds **/
        canvas.drawArc(circleBounds, 360, 360, false, rimPaint);
        /** Boolean variable setting as false **/
        boolean mustInvalidate = false;

        if (isSpinning) {
            // Draw the spinning bar
            mustInvalidate = true;
            /** Delta time **/
            long deltaTime = SystemClock.uptimeMillis() - lastTimeAnimated;
            float deltaNormalized = deltaTime * spinSpeed / 1000.0f;
/**Update the bar length**/
            updateBarLength(deltaTime);

            mProgress += deltaNormalized;
            if (mProgress > 360) {
                mProgress -= 360f;
            }
            /**Last time animated**/
            lastTimeAnimated = SystemClock.uptimeMillis();

            float from = mProgress - 90;
            float length = BARLENGTH + barExtraLength;

            if (isInEditMode()) {
                from = 0;
                length = 135;
            }

            canvas.drawArc(circleBounds, from, length, false, barPaint);
        } else {
            float oldProgress = mProgress;
            if (Float.floatToIntBits(mProgress) == Float
                    .floatToIntBits(mTargetProgress)) {

                // We smoothly increase the progress bar
                mustInvalidate = true;

                float deltaTime = (float) (SystemClock.uptimeMillis() - lastTimeAnimated) / 1000;
                float deltaNormalized = deltaTime * spinSpeed;

                mProgress = Math.min(mProgress + deltaNormalized,
                        mTargetProgress);
                lastTimeAnimated = SystemClock.uptimeMillis();
            }
            if (Float.floatToIntBits(oldProgress) == Float
                    .floatToIntBits(mProgress)) {
                runCallback();
            }

            float offset = 0.0f;
            float progress = mProgress;
            if (!linearProgress) {
                float factor = 2.0f;
                offset = (float) (1.0f - Math.pow(1.0f - mProgress / 360.0f,
                        2.0f * factor)) * 360.0f;
                progress = (float) (1.0f - Math.pow(1.0f - mProgress / 360.0f,
                        factor)) * 360.0f;
            }

            if (isInEditMode()) {
                progress = 360;
            }

            canvas.drawArc(circleBounds, offset - 90, progress, false, barPaint);
        }

        if (mustInvalidate) {
            invalidate();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.view.View#onVisibilityChanged(android.view.View, int)
     */
    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

        if (visibility == VISIBLE) {
            lastTimeAnimated = SystemClock.uptimeMillis();
        }
    }

    /**
     * Update bar length.
     * 
     * @param deltaTimeInMilliSeconds
     *            the delta time in milli seconds
     */
    private void updateBarLength(long deltaTimeInMilliSeconds) {
        if (pausedTimeWithoutGrowing >= PAUSE_GROWING_TIME) {
            timeStartGrowing += deltaTimeInMilliSeconds;

            if (timeStartGrowing > barSpinCycleTime) {
                // We completed a size change cycle
                // (growing or shrinking)
                timeStartGrowing -= barSpinCycleTime;

                pausedTimeWithoutGrowing = 0;

                barGrowingFromFront = !barGrowingFromFront;
            }

            float distance = (float) Math.cos((timeStartGrowing
                    / barSpinCycleTime + 1)
                    * Math.PI) / 2 + 0.5f;
            float destLength = BAR_MAX_LENGTH - BARLENGTH;

            if (barGrowingFromFront) {
                barExtraLength = distance * destLength;
            } else {
                float newLength = destLength * (1 - distance);
                mProgress += (barExtraLength - newLength);
                barExtraLength = newLength;
            }
        } else {
            pausedTimeWithoutGrowing += deltaTimeInMilliSeconds;
        }
    }

    /**
     * Check if the wheel is currently spinning.
     * 
     * @return true, if is spinning
     */

    public boolean isSpinning() {
        return isSpinning;
    }

    /**
     * Reset the count (in increment mode).
     */
    public void resetCount() {
        mProgress = 0.0f;
        mTargetProgress = 0.0f;
        invalidate();
    }

    /**
     * Turn off spin mode.
     */
    public void stopSpinning() {
        isSpinning = false;
        mProgress = 0.0f;
        mTargetProgress = 0.0f;
        invalidate();
    }

    /**
     * Puts the view on spin mode.
     */
    public void spin() {
        lastTimeAnimated = SystemClock.uptimeMillis();
        isSpinning = true;
        invalidate();
    }

    /**
     * Run callback.
     */
    private void runCallback() {
        if (callback != null) {
            float normalizedProgress = (float) Math
                    .round(mProgress * 100 / 360.0f) / 100;
            callback.onProgressUpdate(normalizedProgress);
        }
    }

    /**
     * Set the progress to a specific value, the bar will smoothly animate until
     * that value.
     * 
     * @param fprogress
     *            the new progress
     */
    public void setProgress(float fprogress) {
        if (isSpinning) {
            mProgress = 0.0f;
            isSpinning = false;

            runCallback();
        }

        if (fprogress > 1.0f) {
            fprogress -= 1.0f;
        } else if (fprogress < 0) {
            fprogress = 0;
        }
        if (Float.floatToIntBits(fprogress) == Float
                .floatToIntBits(mTargetProgress)) {
            return;
        }

        // If we are currently in the right position
        // we set again the last time animated so the
        // animation starts smooth from here
        if (Float.floatToIntBits(mProgress) == Float
                .floatToIntBits(mProgress)) {
            lastTimeAnimated = SystemClock.uptimeMillis();
            return;
        }

        mTargetProgress = Math.min(fprogress * 360.0f, 360.0f);

        invalidate();
    }

    /**
     * Set the progress to a specific value, the bar will be set instantly to
     * that value.
     * 
     * @param progress
     *            the progress between 0 and 1
     */
    public void setInstantProgress(float progress) {
        if (isSpinning) {
            mProgress = 0.0f;
            isSpinning = false;
        }

        if (progress > 1.0f) {
            progress -= 1.0f;
        } else if (progress < 0) {
            progress = 0;
        }
        if (Float.floatToIntBits(progress) == Float
                .floatToIntBits(mTargetProgress)) {

            return;
        }

        mTargetProgress = Math.min(progress * 360.0f, 360.0f);
        mProgress = mTargetProgress;
        lastTimeAnimated = SystemClock.uptimeMillis();
        invalidate();
    }

    // Great way to save a view's state
    // http://stackoverflow.com/a/7089687/1991053
    /*
     * (non-Javadoc)
     * 
     * @see android.view.View#onSaveInstanceState()
     */
    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        WheelSavedState ss = new WheelSavedState(superState);

        // We save everything that can be changed at runtime
        ss.mProgress = this.mProgress;
        ss.mTargetProgress = this.mTargetProgress;
        ss.isSpinning = this.isSpinning;
        ss.spinSpeed = this.spinSpeed;
        ss.barWidth = this.barWidth;
        ss.barColor = this.barColor;
        ss.rimWidth = this.rimWidth;
        ss.rimColor = this.rimColor;
        ss.circleRadius = this.circleRadius;
        ss.linearProgress = this.linearProgress;
        ss.fillRadius = this.fillRadius;

        return ss;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.view.View#onRestoreInstanceState(android.os.Parcelable)
     */
    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof WheelSavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        WheelSavedState ss = (WheelSavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        this.mProgress = ss.mProgress;
        this.mTargetProgress = ss.mTargetProgress;
        this.isSpinning = ss.isSpinning;
        this.spinSpeed = ss.spinSpeed;
        this.barWidth = ss.barWidth;
        this.barColor = ss.barColor;
        this.rimWidth = ss.rimWidth;
        this.rimColor = ss.rimColor;
        this.circleRadius = ss.circleRadius;
        this.linearProgress = ss.linearProgress;
        this.fillRadius = ss.fillRadius;
    }

    // ----------------------------------
    // Getters + setters
    // ----------------------------------

    /**
     * Gets the progress.
     * 
     * @return the current progress between 0.0 and 1.0, if the wheel is
     *         indeterminate, then the result is -1
     */
    public float getProgress() {
        return isSpinning ? -1 : mProgress / 360.0f;
    }

    /**
     * Sets the determinate progress mode.
     * 
     * @param isLinear
     *            if the progress should increase linearly
     */
    public void setLinearProgress(boolean isLinear) {
        linearProgress = isLinear;
        if (!isSpinning) {
            invalidate();
        }
    }

    /**
     * Gets the circle radius.
     * 
     * @return the radius of the wheel in pixels
     */
    public int getCircleRadius() {
        return circleRadius;
    }

    /**
     * Sets the radius of the wheel.
     * 
     * @param circleRadius
     *            the expected radius, in pixels
     */
    public void setCircleRadius(int circleRadius) {
        this.circleRadius = circleRadius;
        if (!isSpinning) {
            invalidate();
        }
    }

    /**
     * Gets the bar width.
     * 
     * @return the width of the spinning bar
     */
    public int getBarWidth() {
        return barWidth;
    }

    /**
     * Sets the width of the spinning bar.
     * 
     * @param barWidth
     *            the spinning bar width in pixels
     */
    public void setBarWidth(int barWidth) {
        this.barWidth = barWidth;
        if (!isSpinning) {
            invalidate();
        }
    }

    /**
     * Gets the bar color.
     * 
     * @return the color of the spinning bar
     */
    public int getBarColor() {
        return barColor;
    }

    /**
     * Sets the color of the spinning bar.
     * 
     * @param barColor
     *            The spinning bar color
     */
    public void setBarColor(int barColor) {
        this.barColor = barColor;
        setupPaints();
        if (!isSpinning) {
            invalidate();
        }
    }

    /**
     * Gets the rim color.
     * 
     * @return the color of the wheel's contour
     */
    public int getRimColor() {
        return rimColor;
    }

    /**
     * Sets the color of the wheel's contour.
     * 
     * @param rimColor
     *            the color for the wheel
     */
    public void setRimColor(int rimColor) {
        this.rimColor = rimColor;
        setupPaints();
        if (!isSpinning) {
            invalidate();
        }
    }

    /**
     * Gets the spin speed.
     * 
     * @return the base spinning speed, in full circle turns per second (1.0
     *         equals on full turn in one second), this value also is applied
     *         for the smoothness when setting a progress
     */
    public float getSpinSpeed() {
        return spinSpeed / 360.0f;
    }

    /**
     * Sets the base spinning speed, in full circle turns per second (1.0 equals
     * on full turn in one second), this value also is applied for the
     * smoothness when setting a progress
     * 
     * @param spinSpeed
     *            the desired base speed in full turns per second
     */
    public void setSpinSpeed(float spinSpeed) {
        this.spinSpeed = spinSpeed * 360.0f;
    }

    /**
     * Gets the rim width.
     * 
     * @return the width of the wheel's contour in pixels
     */
    public int getRimWidth() {
        return rimWidth;
    }

    /**
     * Sets the width of the wheel's contour.
     * 
     * @param rimWidth
     *            the width in pixels
     */
    public void setRimWidth(int rimWidth) {
        this.rimWidth = rimWidth;
        if (!isSpinning) {
            invalidate();
        }
    }

    /**
     * The Class WheelSavedState.
     */
    static class WheelSavedState extends BaseSavedState {

        /** The m progress. */
        float mProgress;

        /** The m target progress. */
        float mTargetProgress;

        /** The is spinning. */
        boolean isSpinning;

        /** The spin speed. */
        float spinSpeed;

        /** The bar width. */
        int barWidth;

        /** The bar color. */
        int barColor;

        /** The rim width. */
        int rimWidth;

        /** The rim color. */
        int rimColor;

        /** The circle radius. */
        int circleRadius;

        /** The linear progress. */
        boolean linearProgress;

        /** The fill radius. */
        boolean fillRadius;

        /**
         * Instantiates a new wheel saved state.
         * 
         * @param superState
         *            the super state
         */
        WheelSavedState(Parcelable superState) {
            super(superState);
        }

        /**
         * Instantiates a new wheel saved state.
         * 
         * @param in
         *            the in
         */
        private WheelSavedState(Parcel in) {
            super(in);
            this.mProgress = in.readFloat();
            this.mTargetProgress = in.readFloat();
            this.isSpinning = in.readByte() != 0;
            this.spinSpeed = in.readFloat();
            this.barWidth = in.readInt();
            this.barColor = in.readInt();
            this.rimWidth = in.readInt();
            this.rimColor = in.readInt();
            this.circleRadius = in.readInt();
            this.linearProgress = in.readByte() != 0;
            this.fillRadius = in.readByte() != 0;
        }

        /*
         * (non-Javadoc)
         * 
         * @see android.view.AbsSavedState#writeToParcel(android.os.Parcel, int)
         */
        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeFloat(this.mProgress);
            out.writeFloat(this.mTargetProgress);
            out.writeByte((byte) (isSpinning ? 1 : 0));
            out.writeFloat(this.spinSpeed);
            out.writeInt(this.barWidth);
            out.writeInt(this.barColor);
            out.writeInt(this.rimWidth);
            out.writeInt(this.rimColor);
            out.writeInt(this.circleRadius);
            out.writeByte((byte) (linearProgress ? 1 : 0));
            out.writeByte((byte) (fillRadius ? 1 : 0));
        }

        /** The Constant CREATOR. */
        public static final Creator<WheelSavedState> CREATOR = new Creator<WheelSavedState>() {
            public WheelSavedState createFromParcel(Parcel in) {
                return new WheelSavedState(in);
            }

            public WheelSavedState[] newArray(int size) {
                return new WheelSavedState[size];
            }
        };
    }

    /**
     * The Interface ProgressCallback.
     */
    public interface ProgressCallback {

        /**
         * Method to call when the progress reaches a value in order to avoid
         * float precision issues, the progress is rounded to a long with two
         * decimals.
         * 
         * @param progress
         *            a double value between 0.00 and 1.00 both included
         */
        void onProgressUpdate(float progress);
    }
}
