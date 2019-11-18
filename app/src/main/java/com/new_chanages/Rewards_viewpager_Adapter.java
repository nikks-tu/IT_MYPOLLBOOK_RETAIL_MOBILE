package com.new_chanages;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.new_chanages.fragment.RewardFragment;
import com.new_chanages.fragment.Rewards_Info_Fragment;

/**
 * Created by Techuva on 06-01-2018.
 */

public class Rewards_viewpager_Adapter extends FragmentPagerAdapter {

    final int PAGE_COUNT =2;
    private String[] titles;

    public Rewards_viewpager_Adapter(FragmentManager fm, String[] titles2) {
        super(fm);
        titles=titles2;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            // Open FragmentTab1.java
            case 0:
                return Rewards_Info_Fragment.newInstance("","");
            case 1:
                return RewardFragment.newInstance(position);


        }
        return null;
    }

    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

}
