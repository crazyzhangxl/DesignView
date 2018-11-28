package com.example.apple.designview.activitys.indicator;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.apple.designview.R;
import com.example.apple.designview.views.nice_indicator.NiceViewPagerIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author crazyZhangxl on 2018-11-12 10:24:16.
 * Describe:我所定义的Nice指示器的活动
 */

public class NiceIndicatorActivity extends AppCompatActivity {
    private NiceViewPagerIndicator mNiceViewPagerIndicator;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nice_indicator);
        initViews();
    }

    private void initViews() {
        mNiceViewPagerIndicator = findViewById(R.id.niceIndicator);
        mViewPager = findViewById(R.id.viewPager);
        List<Fragment > fragments = new ArrayList<>();
        List<String> mTitles = new ArrayList<>();
        fragments.add(new FragmentA());
        fragments.add(new FragmentB());
        fragments.add(new FragmentA());
        fragments.add(new FragmentA());
        fragments.add(new FragmentB());
        fragments.add(new FragmentA());
        fragments.add(new FragmentA());
        fragments.add(new FragmentB());
        fragments.add(new FragmentA());
        mTitles.add("英文");
        mTitles.add("推荐");
        mTitles.add("问答");
        mTitles.add("标题世界大战");
        mTitles.add("标题2");
        mTitles.add("标题3");
        mTitles.add("标题66");
        mTitles.add("标题7");
        mTitles.add("标题10");
        mViewPager.setAdapter(new NiceFragmentAdapter(getSupportFragmentManager(),fragments,mTitles));
        mViewPager.setCurrentItem(1);
        mNiceViewPagerIndicator.setIndicatorLengthType(NiceViewPagerIndicator.IndicatorType.ABSOLUTE_LENGTH);
        mNiceViewPagerIndicator.setUpViewPager(mViewPager);
    }


}
