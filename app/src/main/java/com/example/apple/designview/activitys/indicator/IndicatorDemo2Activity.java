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
        initListener();
    }

    private void initListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mIndicator.scroll(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                mIndicator.setSelectedView(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void initViews() {
        mViewPager = findViewById(R.id.viewPager);
        mIndicator = findViewById(R.id.indicator2);
        mIndicator.setIndicatorColor(Color.RED);
        String[] titles = new String[]{"标题1","标题2","标题3"};
        mIndicator.setOnTextSelectedListener(new SimpleViewPagerIndicator.onTextSelectedListener() {
            @Override
            public void onTextSelected(int position) {
                switch (position) {
                    case 0:
                        mViewPager.setCurrentItem(0);
                        break;
                    case 1:
                        mViewPager.setCurrentItem(1);
                        break;
                    case 2:
                        mViewPager.setCurrentItem(2);
                        break;
                    default:
                        break;
                }
            }
        });
        mIndicator.setTitles(titles,1);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new FragmentA());
        fragments.add(new FragmentB());
        fragments.add(new FragmentA());
        mViewPager.setAdapter(new MyFragmentAdapter(getSupportFragmentManager(),fragments));
        mViewPager.setCurrentItem(1);
    }
}
