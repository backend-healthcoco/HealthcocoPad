package com.healthcoco.healthcocoplus.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.healthcoco.healthcocopad.R;

import java.util.ArrayList;

/**
 * Created by Shreshtha on 18-01-2017.
 */

public class InitialScreenViewPagerAapter extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> fragmentsList;

    public InitialScreenViewPagerAapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentsList.get(position);
    }

    @Override
    public int getCount() {
        try {
            if (fragmentsList == null)
                return 0;
            return fragmentsList.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public void setFragmentsList(ArrayList<Fragment> fragmentsList) {
        this.fragmentsList = fragmentsList;
    }

}
