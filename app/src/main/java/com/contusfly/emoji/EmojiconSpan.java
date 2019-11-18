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

package com.contusfly.emoji;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.style.DynamicDrawableSpan;

import com.contusfly.utils.LogMessage;


/**
 * The Class EmojiconSpan.
 *
 * @author Hieu Rocker (rockerhieu@gmail.com)
 */
class EmojiconSpan extends DynamicDrawableSpan {

	/** The m context. */
	private final Context mContext;

	/** The m resource id. */
	private final int mResourceId;

	/** The m size. */
	private final int mSize;

	/** The m drawable. */
	private Drawable mDrawable;

	/**
	 * Instantiates a new emojicon span.
	 *
	 * @param context    the context
	 * @param resourceId the resource id
	 * @param size       the size
	 */
	public EmojiconSpan(Context context, int resourceId, int size) {
		super();
		mContext = context;
		mResourceId = resourceId;
		mSize = size;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.text.style.DynamicDrawableSpan#getDrawable()
	 */
	public Drawable getDrawable() {
		if (mDrawable == null) {
			try {
				mDrawable = mContext.getResources().getDrawable(mResourceId);
				int size = mSize;
				mDrawable.setBounds(0, 0, size, size);
			} catch (Exception e) {
				LogMessage.e(e);
			}
		}
		return mDrawable;
	}
}