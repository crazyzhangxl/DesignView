package com.example.apple.designview.activitys.indicator;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.example.apple.designview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author crazyZhangxl on 2018-11-7 15:56:56.
 * Describe:
 */

public class IndicatorDemo2Activity extends AppCompatActivity {
    private ViewPager mViewPager;
    private SimpleViewPagerIndicator mIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicator_demo2);
        initViews();
    }


    private void initViews() {
        mViewPager = findViewById(R.id.viewPager);
        mIndicator = findViewById(R.id.indicator2);
        mIndicator.setIndicatorColor(Color.RED);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new FragmentA());
        fragments.add(new FragmentB());
        fragments.add(new FragmentA());
        fragments.add(new FragmentB());
        List<String> mTitles = new ArrayList<>();
        mTitles.add("中国");
        mTitles.add("英格兰");
        mTitles.add("美利坚国");
        mTitles.add("嘻嘻");
        mViewPager.setAdapter(new NiceFragmentAdapter(getSupportFragmentManager(),fragments,mTitles));
        mViewPager.setCurrentItem(1);
        mIndicator.setUpWithViewPager(mViewPager);
    }
}
