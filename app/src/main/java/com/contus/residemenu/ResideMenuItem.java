package com.contus.residemenu;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.polls.polls.R;

/**
 * User: special
 * Date: 13-12-10
 * Time: 下午11:05
 * Mail: specialcyci@gmail.com
 */
public class ResideMenuItem extends LinearLayout {

    /**
     * menu item  icon
     */
    private ImageView ivIcon;

    /**
     * menu item  title
     */
    private TextView tvTitle;

    /**
     * Setting the title and icon
     *
     * @param context
     * @param icon
     * @param title
     */
    public ResideMenuItem(Context context, int icon, String title, boolean quickSand) {
        super(context);
        /**View are creating when the activity is started**/
        initViews(context);
        //Setting the icon in image view
        ivIcon.setImageResource(icon);

        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/Quicksand-Light.ttf");

        //Setting the title
        tvTitle.setText(title);

        tvTitle.setTypeface(face);

    }

    /**
     * View are created verticallye
     *
     * @param context context
     */
    private void initViews(Context context) {
        //getView() method to be able to inflate our custom layout & create a View class out of it,
        // we need an instance of LayoutInflater class
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         /* create a new view of my layout and inflate it in the row */
        inflater.inflate(R.layout.residemenu_item, this);
        ivIcon = (ImageView) findViewById(R.id.iv_icon);
        tvTitle = (TextView) findViewById(R.id.tv_title);
    }

    /**
     * set the icon color;
     *
     * @param icon
     */
    public void setIcon(int icon) {
        ivIcon.setImageResource(icon);
    }

    /**
     * set the title with resource
     * ;
     *
     * @param title
     */
    public void setTitle(int title) {
        tvTitle.setText(title);
    }

    /**
     * set the title with string;
     *
     * @param title
     */
    public void setTitle(String title) {
        tvTitle.setText(title);
    }
}
