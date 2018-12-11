package com.example.apple.designview.activitys.draw;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.apple.designview.R;
import com.example.apple.designview.views.ShadowLayerView;

/**
 * @author crazyZhangxl on 2018-11-30 13:05:50.
 * Describe: 设置阴影效果
 */

public class ShadowActivity extends AppCompatActivity {
    private Button mBtnOpen,mBtnClose;
    private ShadowLayerView mShadowLayerView;
    private TextView mTvText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shadow);
        initViews();
    }

    private void initViews() {
        mBtnOpen = findViewById(R.id.open);
        mBtnClose = findViewById(R.id.close);
        mShadowLayerView = findViewById(R.id.shadow);
        mTvText = findViewById(R.id.tvText);
        mBtnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShadowLayerView.setShadow(true);
            }
        });

        mBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShadowLayerView.setShadow(false);
            }
        });

        mTvText.setShadowLayer(1,3,3, Color.RED);
    }
}
