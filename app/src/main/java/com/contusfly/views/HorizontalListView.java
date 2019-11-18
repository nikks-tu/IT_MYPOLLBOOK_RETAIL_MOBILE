/**
 * @category   ContusMessanger
 * @package    com.contusfly.views
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved. 
 * @license    http://www.apache.org/licenses/LICENSE-2.0
 */

// @formatter:off
/*
 * This is based on HorizontalListView.java from: https://github.com/dinocore1/DevsmartLib-Android
 * It has been substantially rewritten and added to from the original version.
 */
// @formatter:on
package com.contusfly.views;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.core.view.ViewCompat;
import androidx.core.widget.EdgeEffectCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.polls.polls.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

// @formatter:off

/**
 * The Class HorizontalListView.
 */
// @formatter:on
@SuppressLint("NewApi")
public class HorizontalListView extends AdapterView<ListAdapter> {

    /** The Constant INSERT_AT_END_OF_LIST. */
    private static final int INSERT_AT_END_OF_LIST = -1;

    /** The Constant INSERT_AT_START_OF_LIST. */
    private static final int INSERT_AT_START_OF_LIST = 0;

    /** The Constant FLING_DEFAULT_ABSORB_VELOCITY. */
    private static final float FLING_DEFAULT_ABSORB_VELOCITY = 30f;

    /** The Constant FLING_FRICTION. */
    private static final float FLING_FRICTION = 0.009f;

    /** The Constant BUNDLE_ID_CURRENT_X. */
    private static final String BUNDLE_ID_CURRENT_X = "BUNDLE_ID_CURRENT_X";

    /** The Constant BUNDLE_ID_PARENT_STATE. */
    private static final String BUNDLE_ID_PARENT_STATE = "BUNDLE_ID_PARENT_STATE";

    /** The m gesture listener. */
    private final GestureListener mGestureListener = new GestureListener();

    /** The m fling tracker. */
    protected Scroller mFlingTracker = new Scroller(getContext());

    /** The m adapter. */
    protected ListAdapter mAdapter;

    /** The m current x. */
    protected int mCurrentX;

    /** The m next x. */
    protected int mNextX;

    /** The m gesture detector. */
    private GestureDetector mGestureDetector;

    /** The m display offset. */
    private int mDisplayOffset;

    /** The m removed views cache. */
    private List<Queue<View>> mRemovedViewsCache = new ArrayList<Queue<View>>();

    /** The m data changed. */
    private boolean mDataChanged = false;

    /** The m rect. */
    private Rect mRect = new Rect();

    /** The m view being touched. */
    private View mViewBeingTouched = null;

    /** The m divider width. */
    private int mDividerWidth = 0;

    /** The m divider. */
    private Drawable mDivider = null;

    /** The m restore x. */
    private Integer mRestoreX = null;

    /** The m max x. */
    private int mMaxX = Integer.MAX_VALUE;

    /** The m left view adapter index. */
    private int mLeftViewAdapterIndex;

    /** The m right view adapter index. */
    private int mRightViewAdapterIndex;

    /** The m currently selected adapter index. */
    private int mCurrentlySelectedAdapterIndex;

    /** The m running out of data listener. */
    private RunningOutOfDataListener mRunningOutOfDataListener = null;

    /** The m running out of data threshold. */
    private int mRunningOutOfDataThreshold = 0;

    /** The m has notified running low on data. */
    private boolean mHasNotifiedRunningLowOnData = false;

    /** The m adapter data observer. */
    private DataSetObserver mAdapterDataObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            mDataChanged = true;

            // Clear so we can notify again as we run out of data
            mHasNotifiedRunningLowOnData = false;

            unpressTouchedChild();

            // Invalidate and request layout to force this view to completely
            // redraw itself
            invalidate();
            requestLayout();
        }

        @Override
        public void onInvalidated() {
            // Clear so we can notify again as we run out of data
            mHasNotifiedRunningLowOnData = false;

            unpressTouchedChild();
            reset();

            // Invalidate and request layout to force this view to completely
            // redraw itself
            invalidate();
            requestLayout();
        }
    };

    /** The m on scroll state changed listener. */
    private OnScrollStateChangedListener mOnScrollStateChangedListener = null;

    /** The m current scroll state. */
    private OnScrollStateChangedListener.ScrollState mCurrentScrollState = OnScrollStateChangedListener.ScrollState.SCROLL_STATE_IDLE;

    /** The m edge glow left. */
    private EdgeEffectCompat mEdgeGlowLeft;

    /** The m edge glow right. */
    private EdgeEffectCompat mEdgeGlowRight;

    /** The m height measure spec. */
    private int mHeightMeasureSpec;

    /** The m block touch action. */
    private boolean mBlockTouchAction = false;

    /**
     * The m is parent verticially scrollable view disallowing intercept touch
     * event.
     */
    private boolean mIsParentVerticiallyScrollableViewDisallowingInterceptTouchEvent = false;

    /** The m on click listener. */
    private OnClickListener mOnClickListener;

    /** The m delayed layout. */
    private Runnable mDelayedLayout = new Runnable() {
        @Override
        public void run() {
            requestLayout();
        }
    };

    /**
     * Instantiates a new horizontal list view.
     *
     * @param context
     *            the context
     * @param attrs
     *            the attrs
     */
    public HorizontalListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mEdgeGlowLeft = new EdgeEffectCompat(context);
        mEdgeGlowRight = new EdgeEffectCompat(context);
        mGestureDetector = new GestureDetector(context, mGestureListener);
        bindGestureDetector();
        initView();
        retrieveXmlConfiguration(context, attrs);
        setWillNotDraw(false);

        // If the OS version is high enough then set the friction on the fling
        // tracker */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            HoneycombPlus.setFriction(mFlingTracker, FLING_FRICTION);
        }
    }

    /**
     * Bind gesture detector.
     */
    private void bindGestureDetector() {
        // Generic touch listener that can be applied to any view that needs to
        // process gestures
        final OnTouchListener gestureListenerHandler = new OnTouchListener() {
            @Override
            public boolean onTouch(final View v, final MotionEvent event) {
                // Delegate the touch event to our gesture detector
                return mGestureDetector.onTouchEvent(event);
            }
        };

        setOnTouchListener(gestureListenerHandler);
    }

    /**
     * Request parent list view to not intercept touch events.
     *
     * @param disallowIntercept
     *            the disallow intercept
     */
    private void requestParentListViewToNotInterceptTouchEvents(
            Boolean disallowIntercept) {
        // Prevent calling this more than once needlessly
        if (mIsParentVerticiallyScrollableViewDisallowingInterceptTouchEvent != disallowIntercept) {
            View view = this;

            while (view.getParent() instanceof View) {
                // If the parent is a ListView or ScrollView then disallow
                // intercepting of touch events
                if (view.getParent() instanceof ListView
                        || view.getParent() instanceof ScrollView) {
                    view.getParent().requestDisallowInterceptTouchEvent(
                            disallowIntercept);
                    mIsParentVerticiallyScrollableViewDisallowingInterceptTouchEvent = disallowIntercept;
                    return;
                }

                view = (View) view.getParent();
            }
        }
    }

    /**
     * Retrieve xml configuration.
     *
     * @param context
     *            the context
     * @param attrs
     *            the attrs
     */
    private void retrieveXmlConfiguration(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.HorizontalListView);

            // Get the provided drawable from the XML
            final Drawable d = a
                    .getDrawable(R.styleable.HorizontalListView_android_divider);
            if (d != null) {
                // If a drawable is provided to use as the divider then use its
                // intrinsic width for the divider width
                setDivider(d);
            }

            // If a width is explicitly specified then use that width
            final int dividerWidth = a.getDimensionPixelSize(
                    R.styleable.HorizontalListView_dividerWidth, 0);
            if (dividerWidth != 0) {
                setDividerWidth(dividerWidth);
            }

            a.recycle();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.view.View#onSaveInstanceState()
     */
    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();

        // Add the parent state to the bundle
        bundle.putParcelable(BUNDLE_ID_PARENT_STATE,
                super.onSaveInstanceState());

        // Add our state to the bundle
        bundle.putInt(BUNDLE_ID_CURRENT_X, mCurrentX);

        return bundle;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.view.View#onRestoreInstanceState(android.os.Parcelable)
     */
    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;

            // Restore our state from the bundle
            mRestoreX = Integer.valueOf((bundle.getInt(BUNDLE_ID_CURRENT_X)));

            // Restore out parent's state from the bundle
            super.onRestoreInstanceState(bundle
                    .getParcelable(BUNDLE_ID_PARENT_STATE));
        }
    }

    /**
     * Sets the divider.
     *
     * @param divider
     *            the new divider
     */
    public void setDivider(Drawable divider) {
        mDivider = divider;

        if (divider != null) {
            setDividerWidth(divider.getIntrinsicWidth());
        } else {
            setDividerWidth(0);
        }
    }

    /**
     * Sets the divider width.
     *
     * @param width
     *            the new divider width
     */
    public void setDividerWidth(int width) {
        mDividerWidth = width;

        // Force the view to rerender itself
        requestLayout();
        invalidate();
    }

    /**
     * Inits the view.
     */
    private void initView() {
        mLeftViewAdapterIndex = -1;
        mRightViewAdapterIndex = -1;
        mDisplayOffset = 0;
        mCurrentX = 0;
        mNextX = 0;
        mMaxX = Integer.MAX_VALUE;
        setCurrentScrollState(OnScrollStateChangedListener.ScrollState.SCROLL_STATE_IDLE);
    }

    /**
     * Reset.
     */
    private void reset() {
        initView();
        removeAllViewsInLayout();
        requestLayout();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.AdapterView#setSelection(int)
     */
    @Override
    public void setSelection(int position) {
        mCurrentlySelectedAdapterIndex = position;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.AdapterView#getSelectedView()
     */
    @Override
    public View getSelectedView() {
        return getChild(mCurrentlySelectedAdapterIndex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.AdapterView#getAdapter()
     */
    @Override
    public ListAdapter getAdapter() {
        return mAdapter;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.AdapterView#setAdapter(android.widget.Adapter)
     */
    @Override
    public void setAdapter(ListAdapter adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mAdapterDataObserver);
        }

        if (adapter != null) {
            // Clear so we can notify again as we run out of data
            mHasNotifiedRunningLowOnData = false;

            mAdapter = adapter;
            mAdapter.registerDataSetObserver(mAdapterDataObserver);
        }

        initializeRecycledViewCache(mAdapter.getViewTypeCount());
        reset();
    }

    /**
     * Initialize recycled view cache.
     *
     * @param viewTypeCount
     *            the view type count
     */
    private void initializeRecycledViewCache(int viewTypeCount) {
        // The cache is created such that the response from
        // mAdapter.getItemViewType is the array index to the correct cache for
        // that item.
        mRemovedViewsCache.clear();
        for (int i = 0; i < viewTypeCount; i++) {
            mRemovedViewsCache.add(new LinkedList<View>());
        }
    }

    /**
     * Gets the recycled view.
     *
     * @param adapterIndex
     *            the adapter index
     * @return the recycled view
     */
    private View getRecycledView(int adapterIndex) {
        int itemViewType = mAdapter.getItemViewType(adapterIndex);

        if (isItemViewTypeValid(itemViewType)) {
            return mRemovedViewsCache.get(itemViewType).poll();
        }

        return null;
    }

    /**
     * Recycle view.
     *
     * @param adapterIndex
     *            the adapter index
     * @param view
     *            the view
     */
    private void recycleView(int adapterIndex, View view) {
        // There is one Queue of views for each different type of view.
        // Just add the view to the pile of other views of the same type.
        // The order they are added and removed does not matter.
        int itemViewType = mAdapter.getItemViewType(adapterIndex);
        if (isItemViewTypeValid(itemViewType)) {
            mRemovedViewsCache.get(itemViewType).offer(view);
        }
    }

    /**
     * Checks if is item view type valid.
     *
     * @param itemViewType
     *            the item view type
     * @return true, if is item view type valid
     */
    private boolean isItemViewTypeValid(int itemViewType) {
        return itemViewType < mRemovedViewsCache.size();
    }

    /**
     * Adds the and measure child.
     *
     * @param child
     *            the child
     * @param viewPos
     *            the view pos
     */
    private void addAndMeasureChild(final View child, int viewPos) {
        LayoutParams params = getLayoutParams(child);
        addViewInLayout(child, viewPos, params, true);
        measureChild(child);
    }

    /**
     * Measure child.
     *
     * @param child
     *            the child
     */
    private void measureChild(View child) {
        LayoutParams childLayoutParams = getLayoutParams(child);
        int childHeightSpec = ViewGroup.getChildMeasureSpec(mHeightMeasureSpec,
                getPaddingTop() + getPaddingBottom(), childLayoutParams.height);

        int childWidthSpec;
        if (childLayoutParams.width > 0) {
            childWidthSpec = MeasureSpec.makeMeasureSpec(
                    childLayoutParams.width, MeasureSpec.EXACTLY);
        } else {
            childWidthSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }

        child.measure(childWidthSpec, childHeightSpec);
    }

    /**
     * Gets the layout params.
     *
     * @param child
     *            the child
     * @return the layout params
     */
    private LayoutParams getLayoutParams(View child) {
        LayoutParams layoutParams = child.getLayoutParams();
        if (layoutParams == null) {
            // Since this is a horizontal list view default to matching the
            // parents height, and wrapping the width
            layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.MATCH_PARENT);
        }

        return layoutParams;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.AdapterView#onLayout(boolean, int, int, int, int)
     */
    @SuppressLint("WrongCall")
    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
            int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (mAdapter == null) {
            return;
        }

        // Force the OS to redraw this view
        invalidate();

        // If the data changed then reset everything and render from scratch at
        // the same offset as last time
        if (mDataChanged) {
            int oldCurrentX = mCurrentX;
            initView();
            removeAllViewsInLayout();
            mNextX = oldCurrentX;
            mDataChanged = false;
        }

        // If restoring from a rotation
        if (mRestoreX != null) {
            mNextX = mRestoreX;
            mRestoreX = null;
        }

        // If in a fling
        if (mFlingTracker.computeScrollOffset()) {
            // Compute the next position
            mNextX = mFlingTracker.getCurrX();
        }

        // Prevent scrolling past 0 so you can't scroll past the end of the list
        // to the left
        if (mNextX < 0) {
            mNextX = 0;

            // Show an edge effect absorbing the current velocity
            if (mEdgeGlowLeft.isFinished()) {
                mEdgeGlowLeft.onAbsorb((int) determineFlingAbsorbVelocity());
            }

            mFlingTracker.forceFinished(true);
            setCurrentScrollState(OnScrollStateChangedListener.ScrollState.SCROLL_STATE_IDLE);
        } else if (mNextX > mMaxX) {
            // Clip the maximum scroll position at mMaxX so you can't scroll
            // past the end of the list to the right
            mNextX = mMaxX;

            // Show an edge effect absorbing the current velocity
            if (mEdgeGlowRight.isFinished()) {
                mEdgeGlowRight.onAbsorb((int) determineFlingAbsorbVelocity());
            }

            mFlingTracker.forceFinished(true);
            setCurrentScrollState(OnScrollStateChangedListener.ScrollState.SCROLL_STATE_IDLE);
        }

        // Calculate our delta from the last time the view was drawn
        int dx = mCurrentX - mNextX;
        removeNonVisibleChildren(dx);
        fillList(dx);
        positionChildren(dx);

        // Since the view has now been drawn, update our current position
        mCurrentX = mNextX;

        // If we have scrolled enough to lay out all views, then determine the
        // maximum scroll position now
        if (determineMaxX()) {
            // Redo the layout pass since we now know the maximum scroll
            // position
            onLayout(changed, left, top, right, bottom);
            return;
        }

        // If the fling has finished
        if (mFlingTracker.isFinished()) {
            // If the fling just ended
            if (mCurrentScrollState == OnScrollStateChangedListener.ScrollState.SCROLL_STATE_FLING) {
                setCurrentScrollState(OnScrollStateChangedListener.ScrollState.SCROLL_STATE_IDLE);
            }
        } else {
            // Still in a fling so schedule the next frame
            ViewCompat.postOnAnimation(this, mDelayedLayout);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.view.View#getLeftFadingEdgeStrength()
     */
    @Override
    protected float getLeftFadingEdgeStrength() {
        int horizontalFadingEdgeLength = getHorizontalFadingEdgeLength();

        // If completely at the edge then disable the fading edge
        if (mCurrentX == 0) {
            return 0;
        } else if (mCurrentX < horizontalFadingEdgeLength) {
            // We are very close to the edge, so enable the fading edge
            // proportional to the distance from the edge, and the width of the
            // edge effect
            return (float) mCurrentX / horizontalFadingEdgeLength;
        } else {
            // The current x position is more then the width of the fading edge
            // so enable it fully.
            return 1;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.view.View#getRightFadingEdgeStrength()
     */
    @Override
    protected float getRightFadingEdgeStrength() {
        int horizontalFadingEdgeLength = getHorizontalFadingEdgeLength();

        // If completely at the edge then disable the fading edge
        if (mCurrentX == mMaxX) {
            return 0;
        } else if ((mMaxX - mCurrentX) < horizontalFadingEdgeLength) {
            // We are very close to the edge, so enable the fading edge
            // proportional to the distance from the ednge, and the width of the
            // edge effect
            return (float) (mMaxX - mCurrentX) / horizontalFadingEdgeLength;
        } else {
            // The distance from the maximum x position is more then the width
            // of the fading edge so enable it fully.
            return 1;
        }
    }

    /**
     * Determine fling absorb velocity.
     *
     * @return the float
     */
    private float determineFlingAbsorbVelocity() {
        // If the OS version is high enough get the real velocity */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return IceCreamSandwichPlus.getCurrVelocity(mFlingTracker);
        } else {
            // Unable to get the velocity so just return a default.
            // In actuality this is never used since EdgeEffectCompat does not
            // draw anything unless the device is ICS+.
            // Less then ICS EdgeEffectCompat essentially performs a NOP.
            return FLING_DEFAULT_ABSORB_VELOCITY;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.view.View#onMeasure(int, int)
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // Cache off the measure spec
        mHeightMeasureSpec = heightMeasureSpec;
    }

    ;

    /**
     * Determine max x.
     *
     * @return true, if successful
     */
    private boolean determineMaxX() {
        // If the last view has been laid out, then we can determine the maximum
        // x position
        if (isLastItemInAdapter(mRightViewAdapterIndex)) {
            View rightView = getRightmostChild();

            if (rightView != null) {
                int oldMaxX = mMaxX;

                // Determine the maximum x position
                mMaxX = mCurrentX + (rightView.getRight() - getPaddingLeft())
                        - getRenderWidth();

                // Handle the case where the views do not fill at least 1 screen
                if (mMaxX < 0) {
                    mMaxX = 0;
                }

                if (mMaxX != oldMaxX) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Fill list.
     *
     * @param dx
     *            the dx
     */
    private void fillList(final int dx) {
        // Get the rightmost child and determine its right edge
        int edge = 0;
        View child = getRightmostChild();
        if (child != null) {
            edge = child.getRight();
        }

        // Add new children views to the right, until past the edge of the
        // screen
        fillListRight(edge, dx);

        // Get the leftmost child and determine its left edge
        edge = 0;
        child = getLeftmostChild();
        if (child != null) {
            edge = child.getLeft();
        }

        // Add new children views to the left, until past the edge of the screen
        fillListLeft(edge, dx);
    }

    /**
     * Removes the non visible children.
     *
     * @param dx
     *            the dx
     */
    private void removeNonVisibleChildren(final int dx) {
        View child = getLeftmostChild();

        // Loop removing the leftmost child, until that child is on the screen
        while (child != null && child.getRight() + dx <= 0) {
            // The child is being completely removed so remove its width from
            // the display offset and its divider if it has one.
            // To remove add the size of the child and its divider (if it has
            // one) to the offset.
            // You need to add since its being removed from the left side, i.e.
            // shifting the offset to the right.
            mDisplayOffset += isLastItemInAdapter(mLeftViewAdapterIndex) ? child
                    .getMeasuredWidth() : mDividerWidth
                    + child.getMeasuredWidth();

            // Add the removed view to the cache
            recycleView(mLeftViewAdapterIndex, child);

            // Actually remove the view
            removeViewInLayout(child);

            // Keep track of the adapter index of the left most child
            mLeftViewAdapterIndex++;

            // Get the new leftmost child
            child = getLeftmostChild();
        }

        child = getRightmostChild();

        // Loop removing the rightmost child, until that child is on the screen
        while (child != null && child.getLeft() + dx >= getWidth()) {
            recycleView(mRightViewAdapterIndex, child);
            removeViewInLayout(child);
            mRightViewAdapterIndex--;
            child = getRightmostChild();
        }
    }

    /**
     * Fill list right.
     *
     * @param rightEdge
     *            the right edge
     * @param dx
     *            the dx
     */
    private void fillListRight(int rightEdge, final int dx) {
        // Loop adding views to the right until the screen is filled
        while (rightEdge + dx + mDividerWidth < getWidth()
                && mRightViewAdapterIndex + 1 < mAdapter.getCount()) {
            mRightViewAdapterIndex++;

            // If mLeftViewAdapterIndex < 0 then this is the first time a view
            // is being added, and left == right
            if (mLeftViewAdapterIndex < 0) {
                mLeftViewAdapterIndex = mRightViewAdapterIndex;
            }

            // Get the view from the adapter, utilizing a cached view if one is
            // available
            View child = mAdapter.getView(mRightViewAdapterIndex,
                    getRecycledView(mRightViewAdapterIndex), this);
            addAndMeasureChild(child, INSERT_AT_END_OF_LIST);

            // If first view, then no divider to the left of it, otherwise add
            // the space for the divider width
            rightEdge += (mRightViewAdapterIndex == 0 ? 0 : mDividerWidth)
                    + child.getMeasuredWidth();

            // Check if we are running low on data so we can tell listeners to
            // go get more
            determineIfLowOnData();
        }
    }

    /**
     * Fill list left.
     *
     * @param leftEdge
     *            the left edge
     * @param dx
     *            the dx
     */
    private void fillListLeft(int leftEdge, final int dx) {
        // Loop adding views to the left until the screen is filled
        while (leftEdge + dx - mDividerWidth > 0 && mLeftViewAdapterIndex >= 1) {
            mLeftViewAdapterIndex--;
            View child = mAdapter.getView(mLeftViewAdapterIndex,
                    getRecycledView(mLeftViewAdapterIndex), this);
            addAndMeasureChild(child, INSERT_AT_START_OF_LIST);

            // If first view, then no divider to the left of it
            leftEdge -= mLeftViewAdapterIndex == 0 ? child.getMeasuredWidth()
                    : mDividerWidth + child.getMeasuredWidth();

            // If on a clean edge then just remove the child, otherwise remove
            // the divider as well
            mDisplayOffset -= leftEdge + dx == 0 ? child.getMeasuredWidth()
                    : mDividerWidth + child.getMeasuredWidth();
        }
    }

    /**
     * Position children.
     *
     * @param dx
     *            the dx
     */
    private void positionChildren(final int dx) {
        int childCount = getChildCount();

        if (childCount > 0) {
            mDisplayOffset += dx;
            int leftOffset = mDisplayOffset;

            // Loop each child view
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                int left = leftOffset + getPaddingLeft();
                int top = getPaddingTop();
                int right = left + child.getMeasuredWidth();
                int bottom = top + child.getMeasuredHeight();

                // Layout the child
                child.layout(left, top, right, bottom);

                // Increment our offset by added child's size and divider width
                leftOffset += child.getMeasuredWidth() + mDividerWidth;
            }
        }
    }

    /**
     * Gets the leftmost child.
     *
     * @return the leftmost child
     */
    private View getLeftmostChild() {
        return getChildAt(0);
    }

    /**
     * Gets the rightmost child.
     *
     * @return the rightmost child
     */
    private View getRightmostChild() {
        return getChildAt(getChildCount() - 1);
    }

    /**
     * Gets the child.
     *
     * @param adapterIndex
     *            the adapter index
     * @return the child
     */
    private View getChild(int adapterIndex) {
        if (adapterIndex >= mLeftViewAdapterIndex
                && adapterIndex <= mRightViewAdapterIndex) {
            return getChildAt(adapterIndex - mLeftViewAdapterIndex);
        }

        return null;
    }

    /**
     * Gets the child index.
     *
     * @param x
     *            the x
     * @param y
     *            the y
     * @return the child index
     */
    private int getChildIndex(final int x, final int y) {
        int childCount = getChildCount();

        for (int index = 0; index < childCount; index++) {
            getChildAt(index).getHitRect(mRect);
            if (mRect.contains(x, y)) {
                return index;
            }
        }

        return -1;
    }

    /**
     * Checks if is last item in adapter.
     *
     * @param index
     *            the index
     * @return true, if is last item in adapter
     */
    private boolean isLastItemInAdapter(int index) {
        return index == mAdapter.getCount() - 1;
    }

    /**
     * Gets the render height.
     *
     * @return the render height
     */
    private int getRenderHeight() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }

    /**
     * Gets the render width.
     *
     * @return the render width
     */
    private int getRenderWidth() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    /**
     * Scroll to.
     *
     * @param x
     *            the x
     */
    public void scrollTo(int x) {
        mFlingTracker.startScroll(mNextX, 0, x - mNextX, 0);
        setCurrentScrollState(OnScrollStateChangedListener.ScrollState.SCROLL_STATE_FLING);
        requestLayout();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.AdapterView#getFirstVisiblePosition()
     */
    @Override
    public int getFirstVisiblePosition() {
        return mLeftViewAdapterIndex;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.AdapterView#getLastVisiblePosition()
     */
    @Override
    public int getLastVisiblePosition() {
        return mRightViewAdapterIndex;
    }

    /**
     * Draw edge glow.
     *
     * @param canvas
     *            the canvas
     */
    private void drawEdgeGlow(Canvas canvas) {
        if (mEdgeGlowLeft != null && !mEdgeGlowLeft.isFinished()
                && isEdgeGlowEnabled()) {
            // The Edge glow is meant to come from the top of the screen, so
            // rotate it to draw on the left side.
            final int restoreCount = canvas.save();
            final int height = getHeight();

            canvas.rotate(-90, 0, 0);
            canvas.translate(-height + getPaddingBottom(), 0);

            mEdgeGlowLeft.setSize(getRenderHeight(), getRenderWidth());
            if (mEdgeGlowLeft.draw(canvas)) {
                invalidate();
            }

            canvas.restoreToCount(restoreCount);
        } else if (mEdgeGlowRight != null && !mEdgeGlowRight.isFinished()
                && isEdgeGlowEnabled()) {
            // The Edge glow is meant to come from the top of the screen, so
            // rotate it to draw on the right side.
            final int restoreCount = canvas.save();
            final int width = getWidth();

            canvas.rotate(90, 0, 0);
            canvas.translate(getPaddingTop(), -width);
            mEdgeGlowRight.setSize(getRenderHeight(), getRenderWidth());
            if (mEdgeGlowRight.draw(canvas)) {
                invalidate();
            }

            canvas.restoreToCount(restoreCount);
        }
    }

    /**
     * Draw dividers.
     *
     * @param canvas
     *            the canvas
     */
    private void drawDividers(Canvas canvas) {
        final int count = getChildCount();

        // Only modify the left and right in the loop, we set the top and bottom
        // here since they are always the same
        final Rect bounds = mRect;
        mRect.top = getPaddingTop();
        mRect.bottom = mRect.top + getRenderHeight();

        // Draw the list dividers
        for (int i = 0; i < count; i++) {
            // Don't draw a divider to the right of the last item in the adapter
            if (!(i == count - 1 && isLastItemInAdapter(mRightViewAdapterIndex))) {
                View child = getChildAt(i);

                bounds.left = child.getRight();
                bounds.right = child.getRight() + mDividerWidth;

                // Clip at the left edge of the screen
                if (bounds.left < getPaddingLeft()) {
                    bounds.left = getPaddingLeft();
                }

                // Clip at the right edge of the screen
                if (bounds.right > getWidth() - getPaddingRight()) {
                    bounds.right = getWidth() - getPaddingRight();
                }

                // Draw a divider to the right of the child
                drawDivider(canvas, bounds);

                // If the first view, determine if a divider should be shown to
                // the left of it.
                // A divider should be shown if the left side of this view does
                // not fill to the left edge of the screen.
                if (i == 0 && child.getLeft() > getPaddingLeft()) {
                    bounds.left = getPaddingLeft();
                    bounds.right = child.getLeft();
                    drawDivider(canvas, bounds);
                }
            }
        }
    }

    /**
     * Draw divider.
     *
     * @param canvas
     *            the canvas
     * @param bounds
     *            the bounds
     */
    private void drawDivider(Canvas canvas, Rect bounds) {
        if (mDivider != null) {
            mDivider.setBounds(bounds);
            mDivider.draw(canvas);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.view.View#onDraw(android.graphics.Canvas)
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawDividers(canvas);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.view.ViewGroup#dispatchDraw(android.graphics.Canvas)
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawEdgeGlow(canvas);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.view.ViewGroup#dispatchSetPressed(boolean)
     */
    @Override
    protected void dispatchSetPressed(boolean pressed) {
        // Don't dispatch setPressed to our children. We call setPressed on
        // ourselves to
        // get the selector in the right state, but we don't want to press each
        // child.
    }

    /**
     * On fling.
     *
     * @param e1
     *            the e1
     * @param e2
     *            the e2
     * @param velocityX
     *            the velocity x
     * @param velocityY
     *            the velocity y
     * @return true, if successful
     */
    protected boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
            float velocityY) {
        mFlingTracker.fling(mNextX, 0, (int) -velocityX, 0, 0, mMaxX, 0, 0);
        setCurrentScrollState(OnScrollStateChangedListener.ScrollState.SCROLL_STATE_FLING);
        requestLayout();
        return true;
    }

    /**
     * On down.
     *
     * @param e
     *            the e
     * @return true, if successful
     */
    protected boolean onDown(MotionEvent e) {
        // If the user just caught a fling, then disable all touch actions until
        // they release their finger
        mBlockTouchAction = !mFlingTracker.isFinished();

        // Allow a finger down event to catch a fling
        mFlingTracker.forceFinished(true);
        setCurrentScrollState(OnScrollStateChangedListener.ScrollState.SCROLL_STATE_IDLE);

        unpressTouchedChild();

        if (!mBlockTouchAction) {
            // Find the child that was pressed
            final int index = getChildIndex((int) e.getX(), (int) e.getY());
            if (index >= 0) {
                // Save off view being touched so it can later be released
                mViewBeingTouched = getChildAt(index);

                if (mViewBeingTouched != null) {
                    // Set the view as pressed
                    mViewBeingTouched.setPressed(true);
                    refreshDrawableState();
                }
            }
        }

        return true;
    }

    /**
     * Unpress touched child.
     */
    private void unpressTouchedChild() {
        if (mViewBeingTouched != null) {
            // Set the view as not pressed
            mViewBeingTouched.setPressed(false);
            refreshDrawableState();

            // Null out the view so we don't leak it
            mViewBeingTouched = null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.view.View#onTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Detect when the user lifts their finger off the screen after a touch
        if (event.getAction() == MotionEvent.ACTION_UP) {
            // If not flinging then we are idle now. The user just finished a
            // finger scroll.
            if (mFlingTracker == null || mFlingTracker.isFinished()) {
                setCurrentScrollState(OnScrollStateChangedListener.ScrollState.SCROLL_STATE_IDLE);
            }

            // Allow the user to interact with parent views
            requestParentListViewToNotInterceptTouchEvents(false);

            releaseEdgeGlow();
        } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
            unpressTouchedChild();
            releaseEdgeGlow();

            // Allow the user to interact with parent views
            requestParentListViewToNotInterceptTouchEvents(false);
        }

        return super.onTouchEvent(event);
    }

    ;

    /**
     * Release edge glow.
     */
    private void releaseEdgeGlow() {
        if (mEdgeGlowLeft != null) {
            mEdgeGlowLeft.onRelease();
        }

        if (mEdgeGlowRight != null) {
            mEdgeGlowRight.onRelease();
        }
    }

    /**
     * Sets the running out of data listener.
     *
     * @param listener
     *            the listener
     * @param numberOfItemsLeftConsideredLow
     *            the number of items left considered low
     */
    public void setRunningOutOfDataListener(RunningOutOfDataListener listener,
            int numberOfItemsLeftConsideredLow) {
        mRunningOutOfDataListener = listener;
        mRunningOutOfDataThreshold = numberOfItemsLeftConsideredLow;
    }

    /**
     * Determine if low on data.
     */
    private void determineIfLowOnData() {
        // Check if the threshold has been reached and a listener is registered
        if (mRunningOutOfDataListener != null
                && mAdapter != null
                && mAdapter.getCount() - (mRightViewAdapterIndex + 1) < mRunningOutOfDataThreshold) {

            // Prevent notification more than once
            if (!mHasNotifiedRunningLowOnData) {
                mHasNotifiedRunningLowOnData = true;
                mRunningOutOfDataListener.onRunningOutOfData();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.AdapterView#setOnClickListener(android.view.View.
     * OnClickListener)
     */
    @Override
    public void setOnClickListener(OnClickListener listener) {
        mOnClickListener = listener;
    }

    /**
     * Sets the on scroll state changed listener.
     *
     * @param listener
     *            the new on scroll state changed listener
     */
    public void setOnScrollStateChangedListener(
            OnScrollStateChangedListener listener) {
        mOnScrollStateChangedListener = listener;
    }

    /**
     * Sets the current scroll state.
     *
     * @param newScrollState
     *            the new current scroll state
     */
    private void setCurrentScrollState(
            OnScrollStateChangedListener.ScrollState newScrollState) {
        // If the state actually changed then notify listener if there is one
        if (mCurrentScrollState != newScrollState
                && mOnScrollStateChangedListener != null) {
            mOnScrollStateChangedListener.onScrollStateChanged(newScrollState);
        }

        mCurrentScrollState = newScrollState;
    }

    /**
     * Update overscroll animation.
     *
     * @param scrolledOffset
     *            the scrolled offset
     */
    private void updateOverscrollAnimation(final int scrolledOffset) {
        if (mEdgeGlowLeft == null || mEdgeGlowRight == null)
            return;

        // Calculate where the next scroll position would be
        int nextScrollPosition = mCurrentX + scrolledOffset;

        // If not currently in a fling (Don't want to allow fling offset updates
        // to cause over scroll animation)
        if (mFlingTracker == null || mFlingTracker.isFinished()) {
            // If currently scrolled off the left side of the list and the
            // adapter is not empty
            if (nextScrollPosition < 0) {

                // Calculate the amount we have scrolled since last frame
                int overscroll = Math.abs(scrolledOffset);

                // Tell the edge glow to redraw itself at the new offset
                mEdgeGlowLeft.onPull((float) overscroll / getRenderWidth());

                // Cancel animating right glow
                if (!mEdgeGlowRight.isFinished()) {
                    mEdgeGlowRight.onRelease();
                }
            } else if (nextScrollPosition > mMaxX) {
                // Scrolled off the right of the list

                // Calculate the amount we have scrolled since last frame
                int overscroll = Math.abs(scrolledOffset);

                // Tell the edge glow to redraw itself at the new offset
                mEdgeGlowRight.onPull((float) overscroll / getRenderWidth());

                // Cancel animating left glow
                if (!mEdgeGlowLeft.isFinished()) {
                    mEdgeGlowLeft.onRelease();
                }
            }
        }
    }

    /**
     * Checks if is edge glow enabled.
     *
     * @return true, if is edge glow enabled
     */
    private boolean isEdgeGlowEnabled() {
        if (mAdapter == null || mAdapter.isEmpty())
            return false;

        // If the maxx is more then zero then the user can scroll, so the edge
        // effects should be shown
        return mMaxX > 0;
    }

    /**
     * The listener interface for receiving runningOutOfData events. The class
     * that is interested in processing a runningOutOfData event implements this
     * interface, and the object created with that class is registered with a
     * component using the component's
     * <code>addRunningOutOfDataListener<code> method. When
     * the runningOutOfData event occurs, that object's appropriate
     * method is invoked.
     *
     * @see RunningOutOfDataEvent
     */
    public static interface RunningOutOfDataListener {

        /**
         * On running out of data.
         */
        void onRunningOutOfData();
    }

    /**
     * The listener interface for receiving onScrollStateChanged events. The
     * class that is interested in processing a onScrollStateChanged event
     * implements this interface, and the object created with that class is
     * registered with a component using the component's
     * <code>addOnScrollStateChangedListener<code> method. When
     * the onScrollStateChanged event occurs, that object's appropriate
     * method is invoked.
     *
     * @see OnScrollStateChangedEvent
     */
    public interface OnScrollStateChangedListener {

        /**
         * On scroll state changed.
         *
         * @param scrollState
         *            the scroll state
         */
        public void onScrollStateChanged(ScrollState scrollState);

        /**
         * The Enum ScrollState.
         */
        public enum ScrollState {

            /** The scroll state idle. */
            SCROLL_STATE_IDLE,

            /** The scroll state touch scroll. */
            SCROLL_STATE_TOUCH_SCROLL,

            /** The scroll state fling. */
            SCROLL_STATE_FLING
        }
    }

    /**
     * The Class HoneycombPlus.
     */
    @TargetApi(11)
    /** Wrapper class to protect access to API version 11 and above features */
    private static final class HoneycombPlus {
        static {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                throw new RuntimeException(
                        "Should not get to HoneycombPlus class unless sdk is >= 11!");
            }
        }

        /**
         * Sets the friction.
         *
         * @param scroller
         *            the scroller
         * @param friction
         *            the friction
         */
        public static void setFriction(Scroller scroller, float friction) {
            if (scroller != null) {
                scroller.setFriction(friction);
            }
        }
    }

    /**
     * The Class IceCreamSandwichPlus.
     */
    @TargetApi(14)
    /** Wrapper class to protect access to API version 14 and above features */
    private static final class IceCreamSandwichPlus {
        static {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                throw new RuntimeException(
                        "Should not get to IceCreamSandwichPlus class unless sdk is >= 14!");
            }
        }

        /**
         * Gets the curr velocity.
         *
         * @param scroller
         *            the scroller
         * @return the curr velocity
         */
        public static float getCurrVelocity(Scroller scroller) {
            return scroller.getCurrVelocity();
        }
    }

    /**
     * The listener interface for receiving gesture events. The class that is
     * interested in processing a gesture event implements this interface, and
     * the object created with that class is registered with a component using
     * the component's <code>addGestureListener<code> method. When
     * the gesture event occurs, that object's appropriate
     * method is invoked.
     *
     * @see GestureEvent
     */
    private class GestureListener extends
            GestureDetector.SimpleOnGestureListener {

        /*
         * (non-Javadoc)
         * 
         * @see
         * android.view.GestureDetector.SimpleOnGestureListener#onDown(android
         * .view.MotionEvent)
         */
        @Override
        public boolean onDown(MotionEvent e) {
            return HorizontalListView.this.onDown(e);
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * android.view.GestureDetector.SimpleOnGestureListener#onFling(android
         * .view.MotionEvent, android.view.MotionEvent, float, float)
         */
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                float velocityY) {
            return HorizontalListView.this
                    .onFling(e1, e2, velocityX, velocityY);
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * android.view.GestureDetector.SimpleOnGestureListener#onScroll(android
         * .view.MotionEvent, android.view.MotionEvent, float, float)
         */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                float distanceX, float distanceY) {
            // Lock the user into interacting just with this view
            requestParentListViewToNotInterceptTouchEvents(true);

            setCurrentScrollState(OnScrollStateChangedListener.ScrollState.SCROLL_STATE_TOUCH_SCROLL);
            unpressTouchedChild();
            mNextX += (int) distanceX;
            updateOverscrollAnimation(Math.round(distanceX));
            requestLayout();

            return true;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * android.view.GestureDetector.SimpleOnGestureListener#onSingleTapConfirmed
         * (android.view.MotionEvent)
         */
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            unpressTouchedChild();
            OnItemClickListener onItemClickListener = getOnItemClickListener();

            final int index = getChildIndex((int) e.getX(), (int) e.getY());

            // If the tap is inside one of the child views, and we are not
            // blocking touches
            if (index >= 0 && !mBlockTouchAction) {
                View child = getChildAt(index);
                int adapterIndex = mLeftViewAdapterIndex + index;

                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(HorizontalListView.this,
                            child, adapterIndex,
                            mAdapter.getItemId(adapterIndex));
                    return true;
                }
            }

            if (mOnClickListener != null && !mBlockTouchAction) {
                mOnClickListener.onClick(HorizontalListView.this);
            }

            return false;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * android.view.GestureDetector.SimpleOnGestureListener#onLongPress(
         * android.view.MotionEvent)
         */
        @Override
        public void onLongPress(MotionEvent e) {
            unpressTouchedChild();

            final int index = getChildIndex((int) e.getX(), (int) e.getY());
            if (index >= 0 && !mBlockTouchAction) {
                View child = getChildAt(index);
                OnItemLongClickListener onItemLongClickListener = getOnItemLongClickListener();
                if (onItemLongClickListener != null) {
                    int adapterIndex = mLeftViewAdapterIndex + index;
                    boolean handled = onItemLongClickListener.onItemLongClick(
                            HorizontalListView.this, child, adapterIndex,
                            mAdapter.getItemId(adapterIndex));

                    if (handled) {
                        performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                    }
                }
            }
        }
    }
}
