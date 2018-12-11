package com.example.apple.designview.activitys;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.apple.designview.R;

/**
 * @author crazyZhangxl on 2018-12-4 21:10:34.
 * Describe:
 */

public class ScrollShowActivity extends AppCompatActivity {
    private HorizontalScrollView mHorizontalScrollView;
    private Button mBtnOne,mBtnTwo;
    private TextView mTvShow;
    private LinearLayout mLinearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_show);
        initViews();
    }

    private void initViews() {
        mHorizontalScrollView = findViewById(R.id.scrollView);
        mBtnOne = findViewById(R.id.btnLeft);
        mBtnTwo = findViewById(R.id.btnRight);
        mTvShow = findViewById(R.id.tvShow);
        mLinearLayout = findViewById(R.id.ll);
        ViewTreeObserver viewTreeObserver = mHorizontalScrollView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                Log.e("日志", "水平滑动 = "+mHorizontalScrollView.getWidth() );
                Log.e("日志", "线性布局长度 =   "+mLinearLayout.getWidth() );
                Log.e("日志", "scrollView的scrollX =   "+mHorizontalScrollView.getScrollX() );
                Log.e("日志", "线性布局 scrollX =   "+mLinearLayout.getScrollX() );
                mHorizontalScrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        mTvShow.setText("原始scrollX = "+mHorizontalScrollView.getScrollX());
        mBtnOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHorizontalScrollView.scrollTo(-200,0);
                Log.e("日志", "-200 = "+mHorizontalScrollView.getScrollX() );
                Log.e("日志", "线性scrollX =   "+mLinearLayout.getScrollX() );

            }
        });

        mBtnTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHorizontalScrollView.scrollTo(200,0);
                Log.e("日志", "+200 = "+mHorizontalScrollView.getScrollX() );
                Log.e("日志", "线性scrollX =   "+mLinearLayout.getScrollX() );
            }
        });
    }
}
