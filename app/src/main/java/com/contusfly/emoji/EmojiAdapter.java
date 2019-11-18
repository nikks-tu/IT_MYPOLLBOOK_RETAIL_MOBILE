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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.polls.polls.R;

import java.util.List;




/**
 * The Class EmojiAdapter.
 *
 * @author Ankush Sachdeva (sankush@yahoo.co.in)
 */
class EmojiAdapter extends ArrayAdapter<Emojicon> {

	/**
	 * The emoji click listener.
	 */
	EmojiconGridView.OnEmojiconClickedListener emojiClickListener;

	/**
	 * Instantiates a new emoji adapter.
	 *
	 * @param context the context
	 * @param data    the data
	 */
	public EmojiAdapter(Context context, List<Emojicon> data) {
		super(context, R.layout.emojicon_item, data);
	}

	/**
	 * Instantiates a new emoji adapter.
	 *
	 * @param context the context
	 * @param data    the data
	 */
	public EmojiAdapter(Context context, Emojicon[] data) {
		super(context, R.layout.emojicon_item, data);
	}

	/**
	 * Sets the emoji click listener.
	 *
	 * @param listener the new emoji click listener
	 */
	public void setEmojiClickListener(EmojiconGridView.OnEmojiconClickedListener listener) {
		this.emojiClickListener = listener;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			v = View.inflate(getContext(), R.layout.emojicon_item, null);
			ViewHolder holder = new ViewHolder();
			holder.icon = (TextView) v.findViewById(R.id.emojicon_icon);
			v.setTag(holder);
		}
		Emojicon emoji = getItem(position);
		ViewHolder holder = (ViewHolder) v.getTag();
		holder.icon.setText(emoji.getEmoji());
		holder.icon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				emojiClickListener.onEmojiconClicked(getItem(position));
			}
		});
		return v;
	}

	/**
	 * The Class ViewHolder.
	 */
	class ViewHolder {

		/**
		 * The icon.
		 */
		TextView icon;
	}
}