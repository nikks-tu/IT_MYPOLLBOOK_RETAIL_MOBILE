/**
 * @category   ContusMessanger
 * @package    com.contusfly.utils
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved. 
 * @license    http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contusfly.utils;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * The Class WrapContentLayoutManager.
 */
public class WrapContentLayoutManager extends
        LinearLayoutManager {

    /** The Constant CHILD_WIDTH. */
    private static final int CHILD_WIDTH = 0;

    /** The Constant CHILD_HEIGHT. */
    private static final int CHILD_HEIGHT = 1;

    /** The Constant DEFAULT_CHILD_SIZE. */
    private static final int DEFAULT_CHILD_SIZE = 100;

    /** The child dimensions. */
    private final int[] childDimensions = new int[2];

    /** The child size. */
    private int childSize = DEFAULT_CHILD_SIZE;

    /** The has child size. */
    private boolean hasChildSize;

    /**
     * Instantiates a new wrap content layout manager.
     * 
     * @param context
     *            the context
     */
    @SuppressWarnings("UnusedDeclaration")
    public WrapContentLayoutManager(Context context) {
        super(context);
    }

    /**
     * Instantiates a new wrap content layout manager.
     * 
     * @param context
     *            the context
     * @param orientation
     *            the orientation
     * @param reverseLayout
     *            the reverse layout
     */
    @SuppressWarnings("UnusedDeclaration")
    public WrapContentLayoutManager(Context context, int orientation,
            boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    /**
     * Make unspecified spec.
     * 
     * @return the int
     */
    public static int makeUnspecifiedSpec() {
        return View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
    }

    /**
     * On measure.
     * 
     * @param recycler
     *            the recycler
     * @param state
     *            the state
     * @param widthSpec
     *            the width spec
     * @param heightSpec
     *            the height spec
     */
    @Override
    public void onMeasure(RecyclerView.Recycler recycler,
            RecyclerView.State state, int widthSpec, int heightSpec) {
        final int widthMode = View.MeasureSpec.getMode(widthSpec);
        final int heightMode = View.MeasureSpec.getMode(heightSpec);

        final int widthSize = View.MeasureSpec.getSize(widthSpec);
        final int heightSize = View.MeasureSpec.getSize(heightSpec);

        final boolean exactWidth = widthMode == View.MeasureSpec.EXACTLY;
        final boolean exactHeight = heightMode == View.MeasureSpec.EXACTLY;

        final int unspecified = makeUnspecifiedSpec();

        if (exactWidth && exactHeight) {
            // in case of exact calculations for both dimensions let's use
            // default "onMeasure" implementation
            super.onMeasure(recycler, state, widthSpec, heightSpec);
            return;
        }

        final boolean vertical = getOrientation() == VERTICAL;

        initChildDimensions(widthSize, heightSize, vertical);

        int width = 0;
        int height = 0;

        // it's possible to get scrap views in recycler which are bound to old
        // (invalid) adapter entities. This
        // happens because their invalidation happens after "onMeasure" method.
        // As a workaround let's clear the
        // recycler now (it should not cause any performance issues while
        // scrolling as "onMeasure" is never
        // called whiles scrolling)
        recycler.clear();

        final int stateItemCount = state.getItemCount();
        final int adapterItemCount = getItemCount();
        // adapter always contains actual data while state might contain old
        // data (f.e. data before the animation is
        // done). As we want to measure the view with actual data we must use
        // data from the adapter and not from the
        // state
        for (int i = 0; i < adapterItemCount; i++) {
            if (vertical) {
                if (!hasChildSize) {
                    if (i < stateItemCount) {
                        // we should not exceed state count, otherwise we'll get
                        // IndexOutOfBoundsException. For such items
                        // we will use previously calculated dimensions
                        measureChild(recycler, i, widthSpec, unspecified,
                                childDimensions);
                    }
                }
                height += childDimensions[CHILD_HEIGHT];
                if (i == 0) {
                    width = childDimensions[CHILD_WIDTH];
                }
                if (height >= heightSize) {
                    break;
                }
            } else {
                if (!hasChildSize) {
                    if (i < stateItemCount) {
                        // we should not exceed state count, otherwise we'll get
                        // IndexOutOfBoundsException. For such items
                        // we will use previously calculated dimensions
                        measureChild(recycler, i, unspecified, heightSpec,
                                childDimensions);
                    }
                }
                width += childDimensions[CHILD_WIDTH];
                if (i == 0) {
                    height = childDimensions[CHILD_HEIGHT];
                }
                if (width >= widthSize) {
                    break;
                }
            }
        }

        if ((vertical && height < heightSize)
                || (!vertical && width < widthSize)) {
            // we really should wrap the contents of the view, let's do it

            if (exactWidth) {
                width = widthSize;
            } else {
                width += getPaddingLeft() + getPaddingRight();
            }

            if (exactHeight) {
                height = heightSize;
            } else {
                height += getPaddingTop() + getPaddingBottom();
            }

            setMeasuredDimension(width, height);
        } else {
            // if calculated height/width exceeds requested height/width let's
            // use default "onMeasure" implementation
            super.onMeasure(recycler, state, widthSpec, heightSpec);
        }
    }

    /**
     * Inits the child dimensions.
     * 
     * @param width
     *            the width
     * @param height
     *            the height
     * @param vertical
     *            the vertical
     */
    private void initChildDimensions(int width, int height, boolean vertical) {
        if (childDimensions[CHILD_WIDTH] != 0
                || childDimensions[CHILD_HEIGHT] != 0) {
            return;
        }
        if (vertical) {
            childDimensions[CHILD_WIDTH] = width;
            childDimensions[CHILD_HEIGHT] = childSize;
        } else {
            childDimensions[CHILD_WIDTH] = childSize;
            childDimensions[CHILD_HEIGHT] = height;
        }
    }

    /**
     * Sets the orientation.
     * 
     * @param orientation
     *            the new orientation
     */
    @Override
    public void setOrientation(int orientation) {
        // might be called before the constructor of this class is called
        // noinspection ConstantConditions
        if (childDimensions != null) {
            if (getOrientation() != orientation) {
                childDimensions[CHILD_WIDTH] = 0;
                childDimensions[CHILD_HEIGHT] = 0;
            }
        }
        super.setOrientation(orientation);
    }

    /**
     * Clear child size.
     */
    public void clearChildSize() {
        hasChildSize = false;
        setChildSize(DEFAULT_CHILD_SIZE);
    }

    /**
     * Sets the child size.
     * 
     * @param childSize
     *            the new child size
     */
    public void setChildSize(int childSize) {
        hasChildSize = true;
        if (this.childSize != childSize) {
            this.childSize = childSize;
            requestLayout();
        }
    }

    /**
     * Measure child.
     * 
     * @param recycler
     *            the recycler
     * @param position
     *            the position
     * @param widthSpec
     *            the width spec
     * @param heightSpec
     *            the height spec
     * @param dimensions
     *            the dimensions
     */
    private void measureChild(RecyclerView.Recycler recycler, int position,
            int widthSpec, int heightSpec, int[] dimensions) {
        final View child = recycler.getViewForPosition(position);

        final RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) child
                .getLayoutParams();

        final int hPadding = getPaddingLeft() + getPaddingRight();
        final int vPadding = getPaddingTop() + getPaddingBottom();

        final int hMargin = p.leftMargin + p.rightMargin;
        final int vMargin = p.topMargin + p.bottomMargin;

        final int hDecoration = getRightDecorationWidth(child)
                + getLeftDecorationWidth(child);
        final int vDecoration = getTopDecorationHeight(child)
                + getBottomDecorationHeight(child);

        final int childWidthSpec = getChildMeasureSpec(widthSpec, hPadding
                + hMargin + hDecoration, p.width, canScrollHorizontally());
        final int childHeightSpec = getChildMeasureSpec(heightSpec, vPadding
                + vMargin + vDecoration, p.height, canScrollVertically());

        child.measure(childWidthSpec, childHeightSpec);

        dimensions[CHILD_WIDTH] = getDecoratedMeasuredWidth(child)
                + p.leftMargin + p.rightMargin;
        dimensions[CHILD_HEIGHT] = getDecoratedMeasuredHeight(child)
                + p.bottomMargin + p.topMargin;

        recycler.recycleView(child);
    }
}