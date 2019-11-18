/*
 * Copyright 2014 Ankush Sachdeva
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.contusfly.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.widget.TextView;

import com.contusfly.emoji.EmojiconHandler;
import com.contusfly.utils.Utils;
import com.polls.polls.R;


/**
 * The Class CustomTextView.
 *
 * @author Hieu Rocker (rockerhieu@gmail.com).
 */
public class CustomTextView extends TextView {

	/** The m emojicon size. */
	private int mEmojiconSize;

	/** The m text start. */
	private int mTextStart = 0;

	/** The m text length. */
	private int mTextLength = -1;

	/**
	 * The m utils.
	 */
	private Utils mUtils;

	/**
	 * Instantiates a new custom text view.
	 *
	 * @param context the context
	 */
	public CustomTextView(Context context) {
		super(context);
		init(null);
	}

	/**
	 * Instantiates a new custom text view.
	 *
	 * @param context the context
	 * @param attrs   the attrs
	 */
	public CustomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mUtils = new Utils();
		mUtils.init(this, context, attrs);
		init(attrs);
	}

	/**
	 * Instantiates a new custom text view.
	 *
	 * @param context  the context
	 * @param attrs    the attrs
	 * @param defStyle the def style
	 */
	public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mUtils = new Utils();
		mUtils.init(this, context, attrs);
		init(attrs);
	}

	/**
	 * Inits the.
	 *
	 * @param attrs
	 *            the attrs
	 */
	private void init(AttributeSet attrs) {
		if (attrs == null) {
			mEmojiconSize = (int) getTextSize();
		} else {
			TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Emojicon);
			mEmojiconSize = (int) a.getDimension(R.styleable.Emojicon_emojiconSize, getTextSize());
			mTextStart = a.getInteger(R.styleable.Emojicon_emojiconTextStart, 0);
			mTextLength = a.getInteger(R.styleable.Emojicon_emojiconTextLength, -1);
			a.recycle();
		}
		setText(getText());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.TextView#setText(java.lang.CharSequence,
	 * android.widget.TextView.BufferType)
	 */
	@Override
	public void setText(CharSequence text, BufferType type) {
		SpannableStringBuilder builder = new SpannableStringBuilder(text);
		EmojiconHandler.addEmojis(getContext(), builder, mEmojiconSize, mTextStart, mTextLength);
		super.setText(builder, type);
	}

	/**
	 * Set the size of emojicon in pixels.
	 *
	 * @param pixels the new emojicon size
	 */
	public void setEmojiconSize(int pixels) {
		mEmojiconSize = pixels;
	}
}
