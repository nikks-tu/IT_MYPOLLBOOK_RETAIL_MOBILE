/**
 * FontableTextview.java
 * <p/>
 * A widget to used to draw text using given font.
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
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.polls.polls.R;


/**
 * A widget to used to draw text using given font.
 */
public class FontableTextview extends AppCompatTextView {

    /** The m context. */
    private Context mContext;

    /**
     * Instantiates a new fontable textview.
     *
     * @param context
     *            the context
     */
    public FontableTextview(Context context) {
        super(context);
        mContext = context;
    }

    /**
     * Instantiates a new fontable textview.
     *
     * @param context
     *            the context
     * @param attrs
     *            the attrs
     */
    public FontableTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        /** inti method **/
        init(context, attrs, 0);
    }

    /**
     * Instantiates a new fontable textview.
     *
     * @param context
     *            the context
     * @param attrs
     *            the attrs
     * @param defStyle
     *            the def style
     */
    public FontableTextview(Context context, AttributeSet attrs, int defStyle) {
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
            /** type array **/
            TypedArray typedArray = getContext().getTheme()
                    .obtainStyledAttributes(attrs,
                            R.styleable.FontableTextView, defStyle, 0);
            if (typedArray.getBoolean(R.styleable.FontableTextView_canLoadFont,
                    false)) {
                /** Customized typeface **/
                Typeface typeFace = Typeface.createFromAsset(mContext
                        .getAssets(), typedArray
                        .getString(R.styleable.FontableTextView_fontSource));
                setTypeface(typeFace);
            }
        }
    }
}
