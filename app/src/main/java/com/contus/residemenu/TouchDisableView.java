package com.contus.residemenu;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by thonguyen on 15/4/14.
 */
class TouchDisableView extends ViewGroup {
    //View is the base class for widgets, which are used to create interactive UI components
    private View mContent;
   //Boolean touch disabled
    private boolean mTouchDisabled = false;

    /**
     * initializes a new instance of the ListView class.
     *
     * @param context context
     */
    public TouchDisableView(Context context) {
        this(context, null);
    }

    /**
     * initializes a new instance of the ListView class.
     *
     * @param context context
     * @param attrs   attrs
     */
    public TouchDisableView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Set view for the menu
     *
     * @param v View is the base class for widgets, which are used to create interactive UI components
     */
    public void setContent(View v) {
        //If the view is not null remove view
        if (mContent != null) {
            this.removeView(mContent);
        }
        mContent = v;
        //Adding the views
        addView(mContent);
    }

    /**
     * Get the content
     *
     * @return mContent
     */
    public View getContent() {
        return mContent;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//Width
        int width = getDefaultSize(0, widthMeasureSpec);
        //Height
        int height = getDefaultSize(0, heightMeasureSpec);
        //Set emasured dimension
        setMeasuredDimension(width, height);
//Content width
        final int contentWidth = getChildMeasureSpec(widthMeasureSpec, 0, width);
        //Content height
        final int contentHeight = getChildMeasureSpec(heightMeasureSpec, 0, height);
        //Measure the length
        mContent.measure(contentWidth, contentHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //Width
        final int width = r - l;
        //Height
        final int height = b - t;
        //Layout
        mContent.layout(0, 0, width, height);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //return boolean value
        return mTouchDisabled;
    }

    /**
     * Diasable touch
     * @param disableTouch boolean
     */
    void setTouchDisable(boolean disableTouch) {
        mTouchDisabled = disableTouch;
    }

}
