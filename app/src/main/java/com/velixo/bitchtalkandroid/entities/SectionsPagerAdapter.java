package com.velixo.bitchtalkandroid.entities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.velixo.bitchtalkandroid.activities.MainActivity;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private static final int CHATFRAGMENT = 0;
    private static final int SETTINGSFRAGMENT = 1;
    private MainActivity activity;

    public SectionsPagerAdapter(FragmentManager fm, MainActivity activity) {
        super(fm);
        this.activity = activity;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if(position == CHATFRAGMENT) {
            return activity.getChatFragment();
        } else if (position == SETTINGSFRAGMENT) {
            return activity.getSettingsFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}