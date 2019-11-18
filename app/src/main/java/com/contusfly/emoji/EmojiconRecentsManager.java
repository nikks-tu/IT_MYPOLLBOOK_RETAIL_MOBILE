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
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.StringTokenizer;


/**
 * The Class EmojiconRecentsManager.
 *
 * @author Daniele Ricci
 */
public class EmojiconRecentsManager extends ArrayList<Emojicon> {

	/** The Constant PREFERENCE_NAME. */
	private static final String PREFERENCE_NAME = "emojicon";

	/** The Constant PREF_RECENTS. */
	private static final String PREF_RECENTS = "recent_emojis";

	/** The Constant PREF_PAGE. */
	private static final String PREF_PAGE = "recent_page";

	/** The Constant LOCK. */
	private static final Object LOCK = new Object();

	/** The s instance. */
	private static EmojiconRecentsManager sInstance;

	/** The m context. */
	private Context mContext;

	/**
	 * Instantiates a new emojicon recents manager.
	 *
	 * @param context
	 *            the context
	 */
	private EmojiconRecentsManager(Context context) {
		mContext = context.getApplicationContext();
		loadRecents();
	}

	/**
	 * Gets the single instance of EmojiconRecentsManager.
	 *
	 * @param context the context
	 * @return single instance of EmojiconRecentsManager
	 */
	public static EmojiconRecentsManager getInstance(Context context) {
		if (sInstance == null) {
			synchronized (LOCK) {
				if (sInstance == null) {
					sInstance = new EmojiconRecentsManager(context);
				}
			}
		}
		return sInstance;
	}

	/**
	 * Gets the recent page.
	 *
	 * @return the recent page
	 */
	public int getRecentPage() {
		return getPreferences().getInt(PREF_PAGE, 0);
	}

	/**
	 * Sets the recent page.
	 *
	 * @param page the new recent page
	 */
	public void setRecentPage(int page) {
		getPreferences().edit().putInt(PREF_PAGE, page).commit();
	}

	/**
	 * Push.
	 *
	 * @param object the object
	 */
	public void push(Emojicon object) {
		if (contains(object)) {
			super.remove(object);
		}
		add(0, object);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ArrayList#add(java.lang.Object)
	 */
	@Override
	public boolean add(Emojicon object) {
		boolean ret = super.add(object);
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ArrayList#add(int, java.lang.Object)
	 */
	@Override
	public void add(int index, Emojicon object) {
		super.add(index, object);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ArrayList#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(Object object) {
		boolean ret = super.remove(object);
		return ret;
	}

	/**
	 * Gets the preferences.
	 *
	 * @return the preferences
	 */
	private SharedPreferences getPreferences() {
		return mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
	}

	/**
	 * Load recents.
	 */
	private void loadRecents() {
		SharedPreferences prefs = getPreferences();
		String str = prefs.getString(PREF_RECENTS, "");
		StringTokenizer tokenizer = new StringTokenizer(str, "~");
		while (tokenizer.hasMoreTokens()) {
			try {
				add(new Emojicon(tokenizer.nextToken()));
			} catch (NumberFormatException e) {
				// ignored
			}
		}
	}

	/**
	 * Save recents.
	 */
	public void saveRecents() {
		StringBuilder str = new StringBuilder();
		int c = size();
		for (int i = 0; i < c; i++) {
			Emojicon e = get(i);
			str.append(e.getEmoji());
			if (i < (c - 1)) {
				str.append('~');
			}
		}
		SharedPreferences prefs = getPreferences();
		prefs.edit().putString(PREF_RECENTS, str.toString()).apply();
	}

}
