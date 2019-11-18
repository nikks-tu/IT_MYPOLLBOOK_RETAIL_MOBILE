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

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;

import com.polls.polls.R;

import java.util.Arrays;


/**
 * The Class EmojiconGridView.
 *
 * @author Hieu Rocker (rockerhieu@gmail.com)
 * @author Ankush Sachdeva (sankush@yahoo.co.in)
 */
public class EmojiconGridView {

	/**
	 * The root view.
	 */
	public View rootView;

	/**
	 * The m emojicon popup.
	 */
	EmojiconsPopup mEmojiconPopup;

	/**
	 * The m recents.
	 */
	EmojiIconRecent mRecents;

	/**
	 * The m data.
	 */
	Emojicon[] mData;

	/**
	 * Instantiates a new emojicon grid view.
	 *
	 * @param context       the context
	 * @param emojicons     the emojicons
	 * @param recents       the recents
	 * @param emojiconPopup the emojicon popup
	 */
	public EmojiconGridView(Context context, Emojicon[] emojicons, EmojiIconRecent recents,
			EmojiconsPopup emojiconPopup) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		mEmojiconPopup = emojiconPopup;
		rootView = inflater.inflate(R.layout.emojicon_grid, null);
		setRecents(recents);
		GridView gridView = (GridView) rootView.findViewById(R.id.Emoji_GridView);
		if (emojicons == null) {
			mData = EmojiConstants.PEOPLE_DATA;
		} else {
			Object[] o = emojicons;
			mData = Arrays.asList(o).toArray(new Emojicon[o.length]);
		}
		EmojiAdapter mAdapter = new EmojiAdapter(rootView.getContext(), mData);
		mAdapter.setEmojiClickListener(new OnEmojiconClickedListener() {

			@Override
			public void onEmojiconClicked(Emojicon emojicon) {
				if (mEmojiconPopup.onEmojiconClickedListener != null) {
					mEmojiconPopup.onEmojiconClickedListener.onEmojiconClicked(emojicon);
				}
				if (mRecents != null) {
					mRecents.addRecentEmoji(rootView.getContext(), emojicon);
				}
			}
		});
		gridView.setAdapter(mAdapter);
	}

	/**
	 * Sets the recents.
	 *
	 * @param recents
	 *            the new recents
	 */
	private void setRecents(EmojiIconRecent recents) {
		mRecents = recents;
	}

	/**
	 * The listener interface for receiving onEmojiconClicked events. The class
	 * that is interested in processing a onEmojiconClicked event implements
	 * this interface, and the object created with that class is registered with
	 * a component using the component's <code>addOnEmojiconClickedListener
	 * <code> method. When the onEmojiconClicked event occurs, that object's
	 * appropriate method is invoked.
	 */
	public interface OnEmojiconClickedListener {

		/**
		 * On emojicon clicked.
		 *
		 * @param emojicon the emojicon
		 */
		void onEmojiconClicked(Emojicon emojicon);
	}

}
