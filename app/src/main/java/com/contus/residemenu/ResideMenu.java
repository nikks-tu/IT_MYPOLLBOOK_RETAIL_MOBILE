package com.contus.residemenu;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.contus.activity.CustomRequest;
import com.contus.activity.VolleyResponseListener;
import com.contus.app.Constants;
import com.contusfly.MApplication;
import com.contusfly.chooseContactsDb.DatabaseHelper;
import com.contusfly.utils.Utils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.polls.polls.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.contus.app.Constants.PROFILE_USER_NAME;

/**
 * User: special
 * Date: 13-12-10
 * Time: 下午10:44
 * Mail: specialcyci@gmail.com
 */
public class ResideMenu extends FrameLayout {
    //Direction left
    public static final int DIRECTION_LEFT = 0;
    //Direction right
    public static final int DIRECTION_RIGHT = 1;
    //Pressed move horizontal
    private static final int PRESSED_MOVE_HORIZONTAL = 2;
    //Pressed down
    private static final int PRESSED_DOWN = 3;
    //Pressed done
    private static final int PRESSED_DONE = 4;
    //Pressed move vertical
    private static final int PRESSED_MOVE_VERTICAL = 5;
    //Image view shadow
    private ImageView imageViewShadow;
    //Layout left menu
    private LinearLayout layoutLeftMenu;
    //Scroll view left menu
    private ScrollView scrollViewLeftMenu;
    //Scroll view menu
    private ScrollView scrollViewMenu;
    /**
     * Current attaching activity.
     */
    private Activity activity;
    /**
     * The DecorView of current activity.
     */
    private ViewGroup viewDecor;
    //Touch disable view
    private TouchDisableView viewActivity;
    /**
     * The flag of menu opening status.
     */
    private boolean isOpened;
    //scale x
    private float shadowAdjustScaleX;
    //Scale y
    private float shadowAdjustScaleY;
    /**
     * Views which need stop to intercept touch events.
     */
    private List<View> ignoredViews;
    //Left menu items
    private List<ResideMenuItem> leftMenuItems;
    //Right menu items
    private List<ResideMenuItem> rightMenuItems;
    //Display metrics
    private DisplayMetrics displayMetrics = new DisplayMetrics();
    //Menu listner
    private OnMenuListener menuListener;
    //lastRawx
    private float lastRawX;
    //Ignored view
    private boolean isInIgnoredView = false;
    //Scalle direction
    private int scaleDirection = DIRECTION_LEFT;
    //Pressed down
    private int pressedState = PRESSED_DOWN;
    //Disabled swipe direction
    private List<Integer> disabledSwipeDirection = new ArrayList<Integer>();
    // Valid scale factor is between 0.0f and 1.0f.
    private float mScaleValue = 0.5f;
    //image view
    private ImageView icUser;
    //user name
    private TextView txtUserName;
    //actiondown x
    private float lastActionDownX, lastActionDownY;
    private DatabaseHelper db;
    TextView total_rewards_tv;

    /**
     * initializes a new instance of the view class.
     *
     * @param context
     */
    public ResideMenu(Context context) {
        super(context);
        /**View are creating when the activity is started**/
        initViews(context);
    }

    /**
     * initializes a new instance of the ListView class.
     *
     * @param context context
     */
    private void initViews(Context context) {
        //Fresco initiazation
        Fresco.initialize(context);
        //getView() method to be able to inflate our custom layout & create a View class out of it,
        // we need an instance of LayoutInflater class
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        /* create a new view of my layout and inflate it in the row */
        inflater.inflate(R.layout.residemenu, this);
        scrollViewLeftMenu = findViewById(R.id.sv_left_menu);
        imageViewShadow = findViewById(R.id.ic_shadow);
        layoutLeftMenu = findViewById(R.id.layout_left_menu);
        icUser = findViewById(R.id.ic_user);
        txtUserName = findViewById(R.id.txtUserName);
        total_rewards_tv = findViewById(R.id.total_rewards_tv) ;
        db = new DatabaseHelper(context);
        //Setting the user name in text view
        txtUserName.setText(MApplication.getDecodedString(MApplication.getString(context, PROFILE_USER_NAME)));
        //Getting the url from preference
        String imageUrl = MApplication.getString(context, Constants.PROFILE_IMAGE);


        //Setting the uri in preference

            Bitmap image = db.getImageCache("profile_pic");
            if (image != null) {
                icUser.setImageBitmap(image);
            } else {
                Utils.loadImageWithGlide(context, imageUrl, icUser, R.drawable.img_ic_user);
            }

    }



    @Override
    protected boolean fitSystemWindows(Rect insets) {
        // Applies the content insets to the view's padding, consuming that content (modifying the insets to be 0),
        // and returning true. This behavior is off by default and can be enabled through setFitsSystemWindows(boolean)
        // in api14+ devices.
        this.setPadding(viewActivity.getPaddingLeft() + insets.left, viewActivity.getPaddingTop() + insets.top,
                viewActivity.getPaddingRight() + insets.right, viewActivity.getPaddingBottom() + insets.bottom);
        insets.left = insets.top = insets.right = insets.bottom = 0;
        return true;
    }

    /**
     * Set up the activity;
     *
     * @param activity
     */
    public void attachToActivity(Activity activity) {
        initValue(activity);
        setShadowAdjustScaleXByOrientation();
        viewDecor.addView(this, 0);
    }

    /**
     * Init activity
     *
     * @param activity
     */
    private void initValue(Activity activity) {
        //activity
        this.activity = activity;
        //Left menu items
        leftMenuItems = new ArrayList<ResideMenuItem>();
        //Right menu items
        rightMenuItems = new ArrayList<ResideMenuItem>();
        //Ignored views
        ignoredViews = new ArrayList<View>();
        viewDecor = (ViewGroup) activity.getWindow().getDecorView();
        //Touch disable view
        viewActivity = new TouchDisableView(this.activity);
        //View decor
        View mContent = viewDecor.getChildAt(0);
        //Remove view at certain position
        viewDecor.removeViewAt(0);
        //Set content
        viewActivity.setContent(mContent);
        //Adding the view details
        addView(viewActivity);
        //View group
        ViewGroup parent = (ViewGroup) scrollViewLeftMenu.getParent();
        //Remove the view
        parent.removeView(scrollViewLeftMenu);

    }

    /**
     * hadowAdjustScaleXByOrientation
     */
    private void setShadowAdjustScaleXByOrientation() {
        //Orientation
        int orientation = getResources().getConfiguration().orientation;
        //If matches orientation landscape
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //scale x
            shadowAdjustScaleX = 0.034f;
            //scale y
            shadowAdjustScaleY = 0.12f;
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            //scale x
            shadowAdjustScaleX = 0.06f;
            //scale y
            shadowAdjustScaleY = 0.07f;
        }
    }


    /**
     * Add a single items;
     *
     * @param menuItem
     * @param direction
     */
    public void addMenuItem(ResideMenuItem menuItem, int direction) {
        //Direction left
        if (direction == DIRECTION_LEFT) {
            //add menu iterm
            this.leftMenuItems.add(menuItem);
            //add view
            layoutLeftMenu.addView(menuItem);
        } else {
            //add menu item
            this.rightMenuItems.add(menuItem);
        }
    }


    /**
     * Show the menu;
     */
    public void openMenu(int direction) {
        //IMage url
        String imageUrl = MApplication.getString(getContext(), Constants.PROFILE_IMAGE);

        //Setting the uri in prefernce

        Bitmap image = db.getImageCache("profile_pic");
        if (image != null) {
            icUser.setImageBitmap(image);
        } else {
            Utils.loadImageWithGlide(getContext(), imageUrl, icUser, R.drawable.img_ic_user);
        }

        //Setting the user name in text view
        txtUserName.setText(MApplication.getDecodedString(MApplication.getString(getContext(), Constants.PROFILE_USER_NAME)));
        setScaleDirection(direction);
        //boolean true
        isOpened = true;
        //This class plays a set of Animator objects in the specified order.
        // Animations can be set up to play together, in sequence, or after a specified delay.
        AnimatorSet scaleDownActivity = buildScaleDownAnimation(viewActivity, mScaleValue, mScaleValue);
        //This class plays a set of Animator objects in the specified order.
        // Animations can be set up to play together, in sequence, or after a specified delay.
        AnimatorSet scaleDownShadow = buildScaleDownAnimation(imageViewShadow,
                mScaleValue + shadowAdjustScaleX, mScaleValue + shadowAdjustScaleY);
        //This class plays a set of Animator objects in the specified order.
        // Animations can be set up to play together, in sequence, or after a specified delay.
        AnimatorSet alphaMenu = buildMenuAnimation(scrollViewMenu, 1.0f);
        //Adds a listener to the set of listeners that are sent events through the life of an animation, s
        // uch as start, repeat, and end.
        scaleDownShadow.addListener(animationListener);
        //Sets up this AnimatorSet to play all of the supplied animations at the same time.
        scaleDownActivity.playTogether(scaleDownShadow);
        //Sets up this AnimatorSet to play all of the supplied animations at the same time.
        scaleDownActivity.playTogether(alphaMenu);
        //Starting this AnimatorSet will, in turn, start the animations for which it is responsible.
        scaleDownActivity.start();
    }

    /**
     * Close the menu;
     */
    public void closeMenu() {
        //IMage url
        String imageUrl = MApplication.getString(activity, Constants.PROFILE_IMAGE);

        //Setting the uri in image view

        Bitmap image = db.getImageCache("profile_pic");
        if (image != null) {
            icUser.setImageBitmap(image);
        } else {
            Utils.loadImageWithGlide(getContext(), imageUrl, icUser, R.drawable.img_ic_user);
        }
        //boolean false
        isOpened = false;
        //This class plays a set of Animator objects in the specified order.
        // Animations can be set up to play together, in sequence, or after a specified delay.
        AnimatorSet scaleUpActivity = buildScaleUpAnimation(viewActivity, 1.0f, 1.0f);
        //This class plays a set of Animator objects in the specified order.
        // Animations can be set up to play together, in sequence, or after a specified delay.
        AnimatorSet scaleUpShadow = buildScaleUpAnimation(imageViewShadow, 1.0f, 1.0f);
        //This class plays a set of Animator objects in the specified order.
        // Animations can be set up to play together, in sequence, or after a specified delay.
        AnimatorSet alphaMenu = buildMenuAnimation(scrollViewMenu, 0.0f);
        //Adds a listener to the set of listeners that are sent events through the life of an animation, s
        // uch as start, repeat, and end.
        scaleUpActivity.addListener(animationListener);
        //Sets up this AnimatorSet to play all of the supplied animations at the same time.
        scaleUpActivity.playTogether(scaleUpShadow);
        //Sets up this AnimatorSet to play all of the supplied animations at the same time.
        scaleUpActivity.playTogether(alphaMenu);
        //Starting this AnimatorSet will, in turn, start the animations for which it is responsible.
        scaleUpActivity.start();
    }

    /**
     * disabledSwipeDirection
     *
     * @param direction direction
     * @return
     */

    private boolean isInDisableDirection(int direction) {
        return disabledSwipeDirection.contains(direction);
    }

    /**
     * setScaleDirection
     *
     * @param direction direction
     */
    private void setScaleDirection(int direction) {
//screen width
        int screenWidth = getScreenWidth();
        //pivot x
        float pivotX;
        //pivot y
        float pivotY = getScreenHeight() * 0.5f;
//Direction left
        if (direction == DIRECTION_LEFT) {
            //Scroll view menu
            scrollViewMenu = scrollViewLeftMenu;
        }
        //Pivot x
        pivotX = screenWidth * 1.5f;

//Set pivot x
        ViewHelper.setPivotX(viewActivity, pivotX);
        //Set pivot x
        ViewHelper.setPivotY(viewActivity, pivotY);
        //Set pivot x
        ViewHelper.setPivotX(imageViewShadow, pivotX);
        //Set pivot x
        ViewHelper.setPivotY(imageViewShadow, pivotY);
        //Scale direction
        scaleDirection = direction;
    }

    /**
     * return the flag of menu status;
     *
     * @return
     */
    public boolean isOpened() {
        return isOpened;
    }

    //Interface definition for a callback to be invoked when a view is clicked.
    private OnClickListener viewActivityOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            //If it is opened it will close
            if (isOpened()) closeMenu();
        }
    };
    //An animation listener receives notifications from an animation.
// Notifications indicate animation related events, such as the end or the repetition of the animation.
    private Animator.AnimatorListener animationListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            //If it is oprned
            if (isOpened()) {
                //Show scroll view menu
                showScrollViewMenu(scrollViewMenu);
                //Menu listner
                if (menuListener != null)
                    //Open menu
                    menuListener.openMenu();
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            // reset the view
            if (isOpened()) {
                //Set toch disable true
                viewActivity.setTouchDisable(true);
                //Interface definition for a callback to be invoked when a view is clicked.
                viewActivity.setOnClickListener(viewActivityOnClickListener);
            } else {
                //Set touch disable true
                viewActivity.setTouchDisable(false);
                //Interface definition for a callback to be invoked when a view is clicked.
                viewActivity.setOnClickListener(null);
                //Hide scroll
                hideScrollViewMenu(scrollViewLeftMenu);
//Menu listner
                if (menuListener != null)
                    //Close menu
                    menuListener.closeMenu();
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            Log.e("test", "test");
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
            Log.e("test", "test");
        }
    };

    /**
     * A helper method to build scale down animation;
     *
     * @param target
     * @param targetScaleX
     * @param targetScaleY
     * @return
     */
    private AnimatorSet buildScaleDownAnimation(View target, float targetScaleX, float targetScaleY) {
        //This class plays a set of Animator objects in the specified order.
        // Animations can be set up to play together, in sequence, or after a specified delay.
        AnimatorSet scaleDown = new AnimatorSet();
        //Sets up this AnimatorSet to play all of the supplied animations at the same time.
        scaleDown.playTogether(
                ObjectAnimator.ofFloat(target, "scaleX", targetScaleX),
                ObjectAnimator.ofFloat(target, "scaleY", targetScaleY)
        );
        //Defines the interpolator used to smooth the animation movement in time.
        scaleDown.setInterpolator(AnimationUtils.loadInterpolator(activity,
                android.R.anim.decelerate_interpolator));
        //How long this animation should last.
        scaleDown.setDuration(250);
        return scaleDown;
    }

    /**
     * A helper method to build scale up animation;
     *
     * @param target
     * @param targetScaleX
     * @param targetScaleY
     * @return
     */
    private AnimatorSet buildScaleUpAnimation(View target, float targetScaleX, float targetScaleY) {
        //This class plays a set of Animator objects in the specified order.
        // Animations can be set up to play together, in sequence, or after a specified delay.
        AnimatorSet scaleUp = new AnimatorSet();
        //Sets up this AnimatorSet to play all of the supplied animations at the same time.
        scaleUp.playTogether(
                ObjectAnimator.ofFloat(target, "scaleX", targetScaleX),
                ObjectAnimator.ofFloat(target, "scaleY", targetScaleY)
        );
        //How long this animation should last.
        scaleUp.setDuration(250);
        return scaleUp;
    }

    /**
     * A helper method to build scale up animation;
     *
     * @param target
     * @param alpha
     * @return
     */
    private AnimatorSet buildMenuAnimation(View target, float alpha) {
        //This class plays a set of Animator objects in the specified order.
        // Animations can be set up to play together, in sequence, or after a specified delay.
        AnimatorSet alphaAnimation = new AnimatorSet();
        //Sets up this AnimatorSet to play all of the supplied animations at the same time.
        alphaAnimation.playTogether(
                ObjectAnimator.ofFloat(target, "alpha", alpha)
        );
        //How long this animation should last.
        alphaAnimation.setDuration(250);
        return alphaAnimation;
    }

    /**
     * Clear the ignored view list;
     */
    public void clearIgnoredViewList() {
        ignoredViews.clear();
    }

    /**
     * If the motion event was relative to the view
     * which in ignored view list,return true;
     *
     * @param ev
     * @return
     */
    private boolean isInIgnoredView(MotionEvent ev) {
        //Rect values
        Rect rect = new Rect();
        for (View v : ignoredViews) {
            v.getGlobalVisibleRect(rect);
            if (rect.contains((int) ev.getX(), (int) ev.getY()))
                return true;
        }
        return false;
    }

    /**
     * setScaleDirectionByRawX
     *
     * @param currentRawX
     */
    private void setScaleDirectionByRawX(float currentRawX) {
        //If contition matches girection right
        //else condition left
        if (currentRawX < lastRawX)
            setScaleDirection(DIRECTION_RIGHT);
        else
            setScaleDirection(DIRECTION_LEFT);
    }

    /**
     * getTargetScale
     *
     * @param currentRawX
     * @return
     */
    private float getTargetScale(float currentRawX) {
        //Scale float x
        float scaleFloatX = ((currentRawX - lastRawX) / getScreenWidth()) * 0.75f;
        //scale float x
        scaleFloatX = scaleDirection == DIRECTION_RIGHT ? -scaleFloatX : scaleFloatX;
//float scale
        float targetScale = ViewHelper.getScaleX(viewActivity) - scaleFloatX;
        //target scale
        targetScale = targetScale > 1.0f ? 1.0f : targetScale;
        //target scale
        targetScale = targetScale < 0.5f ? 0.5f : targetScale;
        return targetScale;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //current activity scale x
        float currentActivityScaleX = ViewHelper.getScaleX(viewActivity);
        ////Float to init bits
        if (Float.floatToIntBits(currentActivityScaleX) == Float.floatToIntBits(1.0f))
            setScaleDirectionByRawX(ev.getRawX());

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //lastActionDownX
                lastActionDownX = ev.getX();
                //lastActionDownY
                lastActionDownY = ev.getY();
                //isInIgnoredView
                isInIgnoredView = isInIgnoredView(ev) && !isOpened();
//                //pressedState
                pressedState = PRESSED_DOWN;
                break;

            case MotionEvent.ACTION_MOVE:
                //isInIgnoredView
                if (isInIgnoredView || isInDisableDirection(scaleDirection))
                    break;
                //Condition matches
                if (pressedState != PRESSED_DOWN &&
                        pressedState != PRESSED_MOVE_HORIZONTAL)
                    break;
//xooset
                int xOffset = (int) (ev.getX() - lastActionDownX);
                //yoffset
                int yOffset = (int) (ev.getY() - lastActionDownY);
//If pressed down
                if (pressedState == PRESSED_DOWN) {
                    //offase condition matches
                    if (yOffset > 25 || yOffset < -25) {
                        //presed date
                        pressedState = PRESSED_MOVE_VERTICAL;
                        break;
                    }
                    //offaset condition matches
                    if (xOffset < -50 || xOffset > 50) {
                        pressedState = PRESSED_MOVE_HORIZONTAL;
                        //ev.setAction(MotionEvent.ACTION_CANCEL);

                        //if it is ignored view
                        if (isInIgnoredView)
                            break;
                        //pressed date
                        if (pressedState != PRESSED_MOVE_HORIZONTAL)
                            break;
                        //pressed state
                        pressedState = PRESSED_DONE;
                        //If it is opened
                        if (isOpened()) {
                            //current activity scalex
                            if (currentActivityScaleX > 0.56f)
                                //close view
                                closeMenu();
                            else
                                //open menu
                                openMenu(scaleDirection);
                        } else {
                            //current activity
                            if (currentActivityScaleX < 0.94f) {
                                //open menu
                                openMenu(scaleDirection);
                            } else {
                                //close menu
                                closeMenu();
                            }
                        }


                    }
                    //offaset condition horizontal
                } else if (pressedState == PRESSED_MOVE_HORIZONTAL) {
                    if (currentActivityScaleX < 0.95)
                        //show scroll view menu
                        showScrollViewMenu(scrollViewMenu);
                    //taeget scale
                    float targetScale = getTargetScale(ev.getRawX());
                    //setscalex
                    ViewHelper.setScaleX(viewActivity, targetScale);
                    //setscalex
                    ViewHelper.setScaleY(viewActivity, targetScale);
                    //setscalex
                    ViewHelper.setScaleX(imageViewShadow, targetScale + shadowAdjustScaleX);
                    //setscaley
                    ViewHelper.setScaleY(imageViewShadow, targetScale + shadowAdjustScaleY);
                    //set alpha
                    ViewHelper.setAlpha(scrollViewMenu, (1 - targetScale) * 2.0f);
                    //lastrawx
                    lastRawX = ev.getRawX();
                    return true;
                }

                break;

            case MotionEvent.ACTION_UP:
                //if it is ignored view
                if (isInIgnoredView)
                    break;
                //pressed date
                if (pressedState != PRESSED_MOVE_HORIZONTAL)
                    break;
                //pressed state
                pressedState = PRESSED_DONE;
                //If it is opened
                if (isOpened()) {
                    //current activity scalex
                    if (currentActivityScaleX > 0.56f)
                        //close view
                        closeMenu();
                    else
                        //open menu
                        openMenu(scaleDirection);
                } else {
                    //current activity
                    if (currentActivityScaleX < 0.94f) {
                        //open menu
                        openMenu(scaleDirection);
                    } else {
                        //close menu
                        closeMenu();
                    }
                }

                break;

        }
        //last raw
        lastRawX = ev.getRawX();
        return super.dispatchTouchEvent(ev);
    }

    /**
     * getScreenHeight
     *
     * @return displayMetrics.heightPixels
     */
    public int getScreenHeight() {
        //metrics
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    /**
     * getScreenWidth
     *
     * @return displayMetrics.widthPixels
     */
    public int getScreenWidth() {
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * setScaleValue
     *
     * @param scaleValue scaleValue
     */
    public void setScaleValue(float scaleValue) {
        this.mScaleValue = scaleValue;
    }

    public interface OnMenuListener {

        /**
         * This method will be called at the finished time of opening menu animations.
         */
        void openMenu();

        /**
         * This method will be called at the finished time of closing menu animations.
         */
        void closeMenu();
    }

    /**
     * showScrollViewMenu
     *
     * @param scrollViewMenu scrollViewMenu
     */
    private void showScrollViewMenu(ScrollView scrollViewMenu) {
        if (scrollViewMenu != null && scrollViewMenu.getParent() == null) {
            addView(scrollViewMenu);
        }
    }

    /**
     * hideScrollViewMenu
     *
     * @param scrollViewMenu scrollViewMenu
     */
    private void hideScrollViewMenu(ScrollView scrollViewMenu) {
        if (scrollViewMenu != null && scrollViewMenu.getParent() != null) {
            removeView(scrollViewMenu);
        }
    }


}
