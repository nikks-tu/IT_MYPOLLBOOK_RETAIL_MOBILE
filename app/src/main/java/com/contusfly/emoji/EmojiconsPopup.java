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
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.PopupWindow;

import com.polls.polls.R;

import java.util.Arrays;
import java.util.List;


/**
 * The Class EmojiconsPopup.
 *
 * @author Ankush Sachdeva (sankush@yahoo.co.in).
 */
public class EmojiconsPopup extends PopupWindow implements ViewPager.OnPageChangeListener, EmojiIconRecent {

    /**
     * The m emoji tab last selected index.
     */
    private int mEmojiTabLastSelectedIndex = -1;

    /**
     * The m emoji tabs.
     */
    private View[] mEmojiTabs;

    /**
     * The m emojis adapter.
     */
    private PagerAdapter mEmojisAdapter;

    /**
     * The m recents manager.
     */
    private EmojiconRecentsManager mRecentsManager;

    /**
     * The key board height.
     */
    private int keyBoardHeight = 0;

    /**
     * The pending open.
     */
    private Boolean pendingOpen = false;

    /**
     * The is opened.
     */
    private Boolean isOpened = false;

    /**
     * The on emojicon clicked listener.
     */
    EmojiconGridView.OnEmojiconClickedListener onEmojiconClickedListener;

    /**
     * The on emojicon backspace clicked listener.
     */
    OnEmojiconBackspaceClickedListener onEmojiconBackspaceClickedListener;

    /**
     * The on soft keyboard open close listener.
     */
    OnSoftKeyboardOpenCloseListener onSoftKeyboardOpenCloseListener;

    /**
     * The root view.
     */
    View rootView;

    /**
     * The m context.
     */
    Context mContext;

    /**
     * The emojis pager.
     */
    private ViewPager emojisPager;

    /**
     * Constructor.
     *
     * @param rootView The top most layout in your view hierarchy. The difference of                 this view and the screen height will be used to calculate the                 keyboard height.
     * @param mContext The context of current activity.
     */
    public EmojiconsPopup(View rootView, Context mContext) {
        super(mContext);
        this.mContext = mContext;
        this.rootView = rootView;
        View customView = createCustomView();
        setContentView(customView);
        setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setSize((int) mContext.getResources().getDimension(R.dimen.keyboard_height), LayoutParams.MATCH_PARENT);
    }

    /**
     * Set the listener for the event of keyboard opening or closing.
     *
     * @param listener the new on soft keyboard open close listener
     */
    public void setOnSoftKeyboardOpenCloseListener(OnSoftKeyboardOpenCloseListener listener) {
        this.onSoftKeyboardOpenCloseListener = listener;
    }

    /**
     * Set the listener for the event when any of the emojicon is clicked.
     *
     * @param listener the new on emojicon clicked listener
     */
    public void setOnEmojiconClickedListener(EmojiconGridView.OnEmojiconClickedListener listener) {
        this.onEmojiconClickedListener = listener;
    }

    /**
     * Set the listener for the event when backspace on emojicon popup is
     * clicked.
     *
     * @param listener the new on emojicon backspace clicked listener
     */
    public void setOnEmojiconBackspaceClickedListener(OnEmojiconBackspaceClickedListener listener) {
        this.onEmojiconBackspaceClickedListener = listener;
    }

    /**
     * Use this function to show the emoji popup. NOTE: Since, the soft keyboard
     * sizes are variable on different android devices, the library needs you to
     * open the soft keyboard atleast once before calling this function. If that
     * is not possible see showAtBottomPending() function.
     */
    public void showAtBottom() {
        showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
    }

    /**
     * Use this function when the soft keyboard has not been opened yet. This
     * will show the emoji popup after the keyboard is up next time. Generally,
     * you will be calling InputMethodManager.showSoftInput function after
     * calling this function.
     */
    public void showAtBottomPending() {
        if (isKeyBoardOpen())
            showAtBottom();
        else
            pendingOpen = true;
    }

    /**
     * Checks if is key board open.
     *
     * @return Returns true if the soft keyboard is open, false otherwise.
     */
    public Boolean isKeyBoardOpen() {
        return isOpened;
    }

    /**
     * Dismiss the popup.
     */
    @Override
    public void dismiss() {
        super.dismiss();
        EmojiconRecentsManager.getInstance(mContext).saveRecents();
    }

    /**
     * Call this function to resize the emoji popup according to your soft
     * keyboard size.
     */
    public void setSizeForSoftKeyboard() {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                int screenHeight = getUsableScreenHeight();
                int heightDifference = screenHeight - (r.bottom - r.top);
                int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    heightDifference -= mContext.getResources().getDimensionPixelSize(resourceId);
                }
                if (heightDifference > 100) {
                    keyBoardHeight = heightDifference;
                    setSize(LayoutParams.MATCH_PARENT, keyBoardHeight);
                    if (!isOpened) {
                        if (onSoftKeyboardOpenCloseListener != null)
                            onSoftKeyboardOpenCloseListener.onKeyboardOpen(keyBoardHeight);
                    }
                    isOpened = true;
                    if (pendingOpen) {
                        showAtBottom();
                        pendingOpen = false;
                    }
                } else {
                    isOpened = false;
                    if (onSoftKeyboardOpenCloseListener != null)
                        onSoftKeyboardOpenCloseListener.onKeyboardClose();
                }
            }
        });
    }

    /**
     * Gets the usable screen height.
     *
     * @return the usable screen height
     */
    private int getUsableScreenHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(metrics);
            return metrics.heightPixels;
        } else {
            return rootView.getRootView().getHeight();
        }
    }

    /**
     * Manually set the popup window size.
     *
     * @param width  Width of the popup
     * @param height Height of the popup
     */
    public void setSize(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    /**
     * Creates the custom view.
     *
     * @return the view
     */
    private View createCustomView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.emojicons, null, false);
        emojisPager = (ViewPager) view.findViewById(R.id.emojis_pager);
        emojisPager.addOnPageChangeListener(this);
        EmojiIconRecent recents = this;
        mEmojisAdapter = new EmojisPagerAdapter(Arrays.asList(new EmojiIconRecentGridView(mContext, null, null, this),
                new EmojiconGridView(mContext, EmojiConstants.PEOPLE_DATA, recents, this),
                new EmojiconGridView(mContext, EmojiConstants.NATURE_DATA, recents, this),
                new EmojiconGridView(mContext, EmojiConstants.OBJECTS_DATA, recents, this),
                new EmojiconGridView(mContext, EmojiConstants.OBJECTS_DATA, recents, this),
                new EmojiconGridView(mContext, EmojiConstants.SYMBOLS_DATA, recents, this)));
        emojisPager.setAdapter(mEmojisAdapter);
        mEmojiTabs = new View[6];
        mEmojiTabs[0] = view.findViewById(R.id.emojis_tab_0_recents);
        mEmojiTabs[1] = view.findViewById(R.id.emojis_tab_1_people);
        mEmojiTabs[2] = view.findViewById(R.id.emojis_tab_2_nature);
        mEmojiTabs[3] = view.findViewById(R.id.emojis_tab_3_objects);
        mEmojiTabs[4] = view.findViewById(R.id.emojis_tab_4_cars);
        mEmojiTabs[5] = view.findViewById(R.id.emojis_tab_5_punctuation);
        for (int i = 0; i < mEmojiTabs.length; i++) {
            final int position = i;
            mEmojiTabs[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    emojisPager.setCurrentItem(position);
                }
            });
        }
        view.findViewById(R.id.emojis_backspace).setOnTouchListener(new RepeatListener(1000, 50, new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onEmojiconBackspaceClickedListener != null)
                    onEmojiconBackspaceClickedListener.onEmojiconBackspaceClicked(v);
            }
        }));
        // get last selected page
        mRecentsManager = EmojiconRecentsManager.getInstance(view.getContext());
        int page = mRecentsManager.getRecentPage();
        // last page was recents, check if there are recents to use
        // if none was found, go to page 1
        if (page == 0 && mRecentsManager.size() == 0) {
            page = 1;
        }
        if (page == 0) {
            onPageSelected(page);
        } else {
            emojisPager.setCurrentItem(page, false);
        }
        return view;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.time.emoji.EmojiIconRecent#addRecentEmoji(android.content.
     * Context, com.time.emoji.Emojicon)
     */
    @Override
    public void addRecentEmoji(Context context, Emojicon emojicon) {
        EmojiIconRecentGridView fragment = ((EmojisPagerAdapter) emojisPager.getAdapter()).getRecentFragment();
        fragment.addRecentEmoji(context, emojicon);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled(
     * int, float, int)
     */
    @Override
    public void onPageScrolled(int i, float v, int i2) {
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected(
     * int)
     */
    @Override
    public void onPageSelected(int i) {
        if (mEmojiTabLastSelectedIndex == i) {
            return;
        }
        switch (i) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                if (mEmojiTabLastSelectedIndex >= 0 && mEmojiTabLastSelectedIndex < mEmojiTabs.length) {
                    mEmojiTabs[mEmojiTabLastSelectedIndex].setSelected(false);
                }
                mEmojiTabs[i].setSelected(true);
                mEmojiTabLastSelectedIndex = i;
                mRecentsManager.setRecentPage(i);
                break;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#
     * onPageScrollStateChanged(int)
     */
    @Override
    public void onPageScrollStateChanged(int i) {
    }

    /**
     * The Class EmojisPagerAdapter.
     */
    private static class EmojisPagerAdapter extends PagerAdapter {

        /**
         * The views.
         */
        private List<EmojiconGridView> views;

        /**
         * Gets the recent fragment.
         *
         * @return the recent fragment
         */
        public EmojiIconRecentGridView getRecentFragment() {
            for (EmojiconGridView it : views) {
                if (it instanceof EmojiIconRecentGridView)
                    return (EmojiIconRecentGridView) it;
            }
            return null;
        }

        /**
         * Instantiates a new emojis pager adapter.
         *
         * @param views the views
         */
        public EmojisPagerAdapter(List<EmojiconGridView> views) {
            super();
            this.views = views;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.support.v4.view.PagerAdapter#getCount()
         */
        @Override
        public int getCount() {
            return views.size();
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * android.support.v4.view.PagerAdapter#instantiateItem(android.view.
         * ViewGroup, int)
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = views.get(position).rootView;
            container.addView(v, 0);
            return v;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.support.v4.view.PagerAdapter#destroyItem(android.view.
         * ViewGroup, int, java.lang.Object)
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object view) {
            container.removeView((View) view);
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * android.support.v4.view.PagerAdapter#isViewFromObject(android.view.
         * View, java.lang.Object)
         */
        @Override
        public boolean isViewFromObject(View view, Object key) {
            return key == view;
        }
    }

    /**
     * A class, that can be used as a TouchListener on any view (e.g. a Button).
     * It cyclically runs a clickListener, emulating keyboard-like behaviour.
     * First click is fired immediately, next before initialInterval, and
     * subsequent before normalInterval.
     * <p/>
     * <p/>
     * Interval is scheduled before the onClick completes, so it has to run
     * fast. If it runs slow, it does not generate skipped onClicks.
     */
    public static class RepeatListener implements View.OnTouchListener {

        /**
         * The handler.
         */
        private Handler handler = new Handler();

        /**
         * The initial interval.
         */
        private int initialInterval;

        /**
         * The normal interval.
         */
        private final int normalInterval;

        /**
         * The click listener.
         */
        private final OnClickListener clickListener;

        /**
         * The handler runnable.
         */
        private Runnable handlerRunnable = new Runnable() {
            @Override
            public void run() {
                if (downView == null) {
                    return;
                }
                handler.removeCallbacksAndMessages(downView);
                handler.postAtTime(this, downView, SystemClock.uptimeMillis() + normalInterval);
                clickListener.onClick(downView);
            }
        };

        /**
         * The down view.
         */
        private View downView;

        /**
         * Instantiates a new repeat listener.
         *
         * @param initialInterval The interval before first click event
         * @param normalInterval  The interval before second and subsequent click events
         * @param clickListener   The OnClickListener, that will be called periodically
         */
        public RepeatListener(int initialInterval, int normalInterval, OnClickListener clickListener) {
            if (clickListener == null)
                throw new IllegalArgumentException("null runnable");
            if (initialInterval < 0 || normalInterval < 0)
                throw new IllegalArgumentException("negative interval");
            this.initialInterval = initialInterval;
            this.normalInterval = normalInterval;
            this.clickListener = clickListener;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.view.View.OnTouchListener#onTouch(android.view.View,
         * android.view.MotionEvent)
         */
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downView = view;
                    handler.removeCallbacks(handlerRunnable);
                    handler.postAtTime(handlerRunnable, downView, SystemClock.uptimeMillis() + initialInterval);
                    clickListener.onClick(view);
                    return true;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_OUTSIDE:
                    handler.removeCallbacksAndMessages(downView);
                    downView = null;
                    return true;
            }
            return false;
        }
    }

    /**
     * The listener interface for receiving onEmojiconBackspaceClicked events.
     * The class that is interested in processing a onEmojiconBackspaceClicked
     * event implements this interface, and the object created with that class
     * is registered with a component using the component's
     * <code>addOnEmojiconBackspaceClickedListener<code> method. When the
     * onEmojiconBackspaceClicked event occurs, that object's appropriate method
     * is invoked.
     */
    public interface OnEmojiconBackspaceClickedListener {

        /**
         * On emojicon backspace clicked.
         *
         * @param v the v
         */
        void onEmojiconBackspaceClicked(View v);
    }

    /**
     * The listener interface for receiving onSoftKeyboardOpenClose events. The
     * class that is interested in processing a onSoftKeyboardOpenClose event
     * implements this interface, and the object created with that class is
     * registered with a component using the component's
     * <code>addOnSoftKeyboardOpenCloseListener<code> method. When the
     * onSoftKeyboardOpenClose event occurs, that object's appropriate method is
     * invoked.
     */
    public interface OnSoftKeyboardOpenCloseListener {

        /**
         * On keyboard open.
         *
         * @param keyBoardHeight the key board height
         */
        void onKeyboardOpen(int keyBoardHeight);

        /**
         * On keyboard close.
         */
        void onKeyboardClose();
    }
}