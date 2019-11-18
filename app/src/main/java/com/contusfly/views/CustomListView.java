/**
 * @category   ContusMessanger
 * @package    com.contusfly.views
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved. 
 * @license    http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contusfly.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * The Class CustomListView.
 */
public class CustomListView extends ListView {

    /**
     * Instantiates a new custom list view.
     *
     * @param context
     *            the context
     * @param attrs
     *            the attrs
     */
    public CustomListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Instantiates a new custom list view.
     *
     * @param context
     *            the context
     */
    public CustomListView(Context context) {
        super(context);
    }

    /**
     * Instantiates a new custom list view.
     *
     * @param context
     *            the context
     * @param attrs
     *            the attrs
     * @param defStyle
     *            the def style
     */
    public CustomListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.ListView#onMeasure(int, int)
     */
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}