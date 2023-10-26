package com.healthcoco.healthcocopad.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class CommonViewPagerAdapter extends FragmentStatePagerAdapter {

    protected static final String TAG = CommonViewPagerAdapter.class.getSimpleName();
    private ArrayList<Fragment> fragmentsList;

    public CommonViewPagerAdapter(FragmentManager fm) {
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
