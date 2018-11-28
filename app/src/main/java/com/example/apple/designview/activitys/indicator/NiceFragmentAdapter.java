package com.example.apple.designview.activitys.indicator;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author crazyZhangxl on 2018/11/12.
 * Describe:
 */
public class NiceFragmentAdapter extends FragmentPagerAdapter{
    private List<Fragment> mFragmentList;
    private List<String> mTitles;

    public NiceFragmentAdapter(FragmentManager fm,List<Fragment> fragments,List<String> mTitle) {
        super(fm);
        this.mFragmentList = fragments;
        this.mTitles =  mTitle;
    }


    @Override
    public Fragment getItem(int i) {
        if (mFragmentList != null){
            return mFragmentList.get(i);
        }
        return null;
    }

    @Override
    public int getCount() {
        if (mFragmentList != null){
            return mFragmentList.size();
        }
        return 0;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitles != null){
            return mTitles.get(position);
        }
        return super.getPageTitle(position);
    }
}
