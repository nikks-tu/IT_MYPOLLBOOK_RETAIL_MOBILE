package com.contus.userpolls;

import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.contus.residemenu.MenuActivity;
import com.polls.polls.R;


/**
 * User: special
 * Date: 13-12-22
 * Time: 下午1:33
 * Mail: specialcyci@gmail.com
 */
public class HomeFragment extends Fragment {
//parent view
    private View parentView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.home, container, false);
        //get the views
        Window window = getActivity().getWindow();
        //Changinf the status bar color and hiding the back arror
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Changinf the status bar color and hiding the back arror
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //Changinf the status bar color and hiding the back arror
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //Changinf the status bar color and hiding the back arror
            window.setStatusBarColor(getActivity().getResources().getColor(R.color.color_primary));
        }
        return parentView;
    }

    /**
     * setUpViews
     */
    private void setUpViews() {
        Log.e("homefragment","homefragment");
        //Calling the activity
        MenuActivity parentActivity = (MenuActivity) getActivity();
        //Getting the reside menu
         parentActivity.getResideMenu();

    }

    @Override
    public void onResume() {
        super.onResume();
        //Setting up the views
        setUpViews();
    }
}
