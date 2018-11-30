package com.example.apple.designview.activitys.indicator;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.apple.designview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author crazyZhangxl on 2018-11-2 9:49:55.
 * Describe: 使用布局文件 RadioGroup+底部指示器 实现指示器
 */

public class IndicatorDemo1Activity extends AppCompatActivity {
    private ViewPager mViewPager;

    private RadioGroup mRadioMain;
    private RadioButton mRadioAc1,mRadioAc2,mRadioAc3;
    private ImageView mIvLinear;


    private int lineWidth = 80;
    private int currentPage = -1;
    private int screenWidth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicator_demo1);
        initViews();
        initTabLines();
        initListener();
    }

    private void initListener() {


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // 对于三个标签 那么一共只有四种情况
                //{1. 0->1 1->2 1->0 2->1}
                Log.e("onPageScrolled",position+",   "+positionOffset+",   "+positionOffsetPixels);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mIvLinear.getLayoutParams();
                layoutParams.leftMargin = (int)((position+positionOffset)*getScreenWidthWidth()/3f+getScreenWidthWidth()/6-lineWidth/2);
                mIvLinear.setLayoutParams(layoutParams);
            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                switch (position){
                    case 0:
                        mRadioAc1.setChecked(true);
                        break;
                    case 1:
                        mRadioAc2.setChecked(true);
                        break;
                    case 2:
                        mRadioAc3.setChecked(true);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initTabLines() {
        mRadioMain = findViewById(R.id.rgMain);
        mRadioAc1 = findViewById(R.id.rbAc1);
        mRadioAc2 = findViewById(R.id.rbAc2);
        mRadioAc3 = findViewById(R.id.rbAc3);
        mRadioAc1.setChecked(true);
        mRadioMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbAc1:
                        mRadioAc1.setChecked(true);
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.rbAc2:
                        mRadioAc2.setChecked(true);
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.rbAc3:
                        mRadioAc3.setChecked(true);
                        mViewPager.setCurrentItem(2);
                        break;
                    default:
                        break;
                }
            }
        });

        mIvLinear = findViewById(R.id.imageIndicator);
        //*****
        // 只有其父布局为LinearLatout时，才能够有 LinearLayoutParams,才能够有leftMargin 设置左边间距
        //*****
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mIvLinear.getLayoutParams();
        layoutParams.width = lineWidth;
        layoutParams.leftMargin = getScreenWidthWidth()/6 - lineWidth/2;
        mIvLinear.setLayoutParams(layoutParams);
    }

    private int getScreenWidthWidth(){
        if (screenWidth == 0) {
            // 获取显示屏信息
            Display display = getWindow().getWindowManager().getDefaultDisplay();
            // 得到显示屏宽度
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            screenWidth = metrics.widthPixels;
        }
        return screenWidth;
    }

    private void initViews() {
        mViewPager = findViewById(R.id.viewPager);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new FragmentA());
        fragments.add(new FragmentB());
        fragments.add(new FragmentA());
        mViewPager.setAdapter(new MyFragmentAdapter(getSupportFragmentManager(),fragments));
        mViewPager.setCurrentItem(0);
    }
}
