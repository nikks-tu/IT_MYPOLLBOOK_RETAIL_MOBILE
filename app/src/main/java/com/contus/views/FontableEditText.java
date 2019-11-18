/**
 * FontableEditText.java
 * <p/>
 * A widget to used to draw edit text using given font.
 *
 * @category Contus
 * @package com.cirrusshop.views
 * @version 1.0
 * @author Contus Team <developers@contus.in>
 * @copyright Copyright (C) 2015 Contus. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contus.views;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import com.polls.polls.R;

/**
 * A widget to used to draw edit text using given font.
 */
public class FontableEditText extends EditText {

    /** The m context. */
    private Context mContext;

    /**
     * Instantiates a new fontable edit text.
     *
     * @param context
     *            the context
     */
    public FontableEditText(Context context) {
        super(context);
        mContext = context;
    }

    /**
     * Instantiates a new fontable edit text.
     *
     * @param context
     *            the context
     * @param attrs
     *            the attrs
     */
    public FontableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        /** init method **/
        init(context, attrs, 0);
    }

    /**
     * Instantiates a new fontable edit text.
     *
     * @param context
     *            the context
     * @param attrs
     *            the attrs
     * @param defStyle
     *            the def style
     */
    public FontableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        /** init method **/
        init(context, attrs, defStyle);
    }

    /**
     * Loads the initial resource for the View.
     *
     * @param context
     *            - The Context associated with the view.
     * @param attrs
     *            - The AttributeSet associated with the view.
     * @param defStyle
     *            - The style associated with the view.
     */
    public void init(Context context, AttributeSet attrs, int defStyle) {
        if (!isInEditMode()) {
            mContext = context;
            /** Type array **/
            TypedArray typedArray = getContext().getTheme()
                    .obtainStyledAttributes(attrs,
                            R.styleable.FontableTextView, defStyle, 0);
            if (typedArray.getBoolean(R.styleable.FontableTextView_canLoadFont,
                    false)) {
                /** Type face **/
                Typeface typeFace = Typeface.createFromAsset(mContext
                        .getAssets(), typedArray
                        .getString(R.styleable.FontableTextView_fontSource));
                setTypeface(typeFace);
            }
        }
    }
}
