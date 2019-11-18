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

import java.io.Serializable;


/**
 * The Class Emojicon.
 *
 * @author Hieu Rocker (rockerhieu@gmail.com)
 */
public class Emojicon implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The emoji. */
	private String emoji;

	/**
	 * Instantiates a new emojicon.
	 */
	private Emojicon() {

	}

	/**
	 * From code point.
	 *
	 * @param codePoint the code point
	 * @return the emojicon
	 */
	public static Emojicon fromCodePoint(int codePoint) {
		Emojicon emoji = new Emojicon();
		emoji.emoji = newString(codePoint);
		return emoji;
	}

	/**
	 * From char.
	 *
	 * @param ch the ch
	 * @return the emojicon
	 */
	public static Emojicon fromChar(char ch) {
		Emojicon emoji = new Emojicon();
		emoji.emoji = Character.toString(ch);
		return emoji;
	}

	/**
	 * From chars.
	 *
	 * @param chars the chars
	 * @return the emojicon
	 */
	public static Emojicon fromChars(String chars) {
		Emojicon emoji = new Emojicon();
		emoji.emoji = chars;
		return emoji;
	}

	/**
	 * Instantiates a new emojicon.
	 *
	 * @param emoji the emoji
	 */
	public Emojicon(String emoji) {
		this.emoji = emoji;
	}

	/**
	 * Gets the emoji.
	 *
	 * @return the emoji
	 */
	public String getEmoji() {
		return emoji;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		return o instanceof Emojicon && emoji.equals(((Emojicon) o).emoji);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return emoji.hashCode();
	}

	/**
	 * New string.
	 *
	 * @param codePoint the code point
	 * @return the string
	 */
	public static final String newString(int codePoint) {
		if (Character.charCount(codePoint) == 1) {
			return String.valueOf(codePoint);
		} else {
			return new String(Character.toChars(codePoint));
		}
	}
}
