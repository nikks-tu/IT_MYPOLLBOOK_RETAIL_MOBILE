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
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ImageView;

import com.contusfly.emoji.EmojiconHandler;
import com.contusfly.utils.LogMessage;
import com.contusfly.utils.Utils;

import java.lang.reflect.Field;

import com.polls.polls.R;

import com.polls.polls.R;


/**
 * The Class CustomEditText.
 *
 * @author Hieu Rocker (rockerhieu@gmail.com).
 */
public class CustomEditText extends EditText {

    /**
     * The m emojicon size.
     */
    private int mEmojiconSize;

    /**
     * The m utils.
     */
    private Utils mUtils;
    private int previousSpansLength = 0;
    private boolean isSudoMax = false;

    private ImageView emojiImage;

    /**
     * Instantiates a new custom edit text.
     *
     * @param context the context
     */
    public CustomEditText(Context context) {
        super(context);
        mEmojiconSize = (int) getTextSize();

    }

    /**
     * Instantiates a new custom edit text.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mUtils = new Utils();
        mUtils.init(this, context, attrs);
        init(attrs);
    }

    /**
     * Instantiates a new custom edit text.
     *
     * @param context  the context
     * @param attrs    the attrs
     * @param defStyle the def style
     */
    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mUtils = new Utils();
        mUtils.init(this, context, attrs);
        init(attrs);
    }

    /**
     * Inits the.
     *
     * @param attrs the attrs
     */
    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Emojicon);
        mEmojiconSize = (int) a.getDimension(R.styleable.Emojicon_emojiconSize, getTextSize());
        a.recycle();
        setText(getText());
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.TextView#onTextChanged(java.lang.CharSequence, int,
     * int, int)
     */
    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        EmojiconHandler.addEmojis(getContext(), getText(), mEmojiconSize);
        adjustFilter(getText());
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        EmojiconHandler.addEmojis(getContext(), builder, mEmojiconSize);
        adjustFilter(builder);

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

    public void adjustFilter(Editable afterText) {
        InputFilter[] inputFilters = getFilters();
        if (inputFilters != null) {
            for (int i = 0, size = inputFilters.length; i < size; i++) {
                InputFilter inputFilter = inputFilters[i];
                if (inputFilter instanceof InputFilter.LengthFilter) {
                    InputFilter.LengthFilter lengthFilter = (InputFilter.LengthFilter) inputFilter;

                    int max = -1;
                    try {
                        Field maxLengthField = lengthFilter.getClass().getDeclaredField("mMax");
                        maxLengthField.setAccessible(true);

                        if (maxLengthField.isAccessible()) {
                            max = maxLengthField.getInt(lengthFilter);
                        }
                    } catch (IllegalAccessException e) {
                        LogMessage.e(e);
                    } catch (IllegalArgumentException e) {
                        LogMessage.e(e);
                    } catch (NoSuchFieldException e) {
                        LogMessage.e(e);
                    } // if an Exception is thrown, Log it and return -1

                    max -= previousSpansLength;
                    int currentSpansLength = EmojiconHandler.getSpannableLength(afterText);
                    max += currentSpansLength;

                    if (max - afterText.length() == 1) {
                        if (!isSudoMax) {
                            max += 1;
                            isSudoMax = true;
                        } else if (previousSpansLength == currentSpansLength) {
                            max -= 1;
                            isSudoMax = false;
                        }
                    } else if (max - afterText.length() != 0 && isSudoMax
                            && max - afterText.length() != 2 && previousSpansLength != currentSpansLength) {
                        max -= 1;
                        isSudoMax = false;
                    }
                    previousSpansLength = currentSpansLength;
                    inputFilter = new InputFilter.LengthFilter(max);
                    inputFilters[i] = inputFilter;
                }
            }
            setFilters(inputFilters);
        }
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK &&
                event.getAction() == KeyEvent.ACTION_UP) {
            if(emojiImage!=null)
                emojiImage.setImageResource(R.drawable.ic_smily);
            // do your stuff
            return false;
        }
        return super.onKeyPreIme(keyCode, event);
    }

    public int getTextLength() {
        return EmojiconHandler.getLength(getText());
    }

    public void setEmojiImage(ImageView emojiImage) {
        this.emojiImage = emojiImage;
    }
}
