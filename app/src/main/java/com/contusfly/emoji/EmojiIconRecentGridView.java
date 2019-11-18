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
import android.widget.GridView;

import com.polls.polls.R;


/**
 * The Class EmojiIconRecentGridView.
 *
 * @author Daniele Ricci
 * @author Ankush Sachdeva (sankush@yahoo.co.in)
 */
public class EmojiIconRecentGridView extends EmojiconGridView implements EmojiIconRecent {

	/**
	 * The m adapter.
	 */
	EmojiAdapter mAdapter;

	/**
	 * Instantiates a new emojicon recents grid view.
	 *
	 * @param context        the context
	 * @param emojicons      the emojicons
	 * @param recents        the recents
	 * @param emojiconsPopup the emojicons popup
	 */
	public EmojiIconRecentGridView(Context context, Emojicon[] emojicons, EmojiIconRecent recents,
								   EmojiconsPopup emojiconsPopup) {
		super(context, emojicons, recents, emojiconsPopup);
		EmojiconRecentsManager recents1 = EmojiconRecentsManager.getInstance(rootView.getContext());
		mAdapter = new EmojiAdapter(rootView.getContext(), recents1);
		mAdapter.setEmojiClickListener(new OnEmojiconClickedListener() {

			@Override
			public void onEmojiconClicked(Emojicon emojicon) {
				if (mEmojiconPopup.onEmojiconClickedListener != null) {
					mEmojiconPopup.onEmojiconClickedListener.onEmojiconClicked(emojicon);
				}
			}
		});
		GridView gridView = (GridView) rootView.findViewById(R.id.Emoji_GridView);
		gridView.setAdapter(mAdapter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.time.emoji.EmojiIconRecent#addRecentEmoji(android.content.
	 * Context, com.time.emoji.Emojicon)
	 */
	@Override
	public void addRecentEmoji(Context context, Emojicon emojicon) {
		EmojiconRecentsManager recents = EmojiconRecentsManager.getInstance(context);
		recents.push(emojicon);

		// notify dataset changed
		if (mAdapter != null)
			mAdapter.notifyDataSetChanged();
	}

}
