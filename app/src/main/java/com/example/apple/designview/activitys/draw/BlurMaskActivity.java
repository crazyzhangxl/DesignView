package com.example.apple.designview.activitys.draw;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.apple.designview.R;
import com.example.apple.designview.views.BlurMaskView;

/**
 * @author crazyZhangxl on 2018-11-30 13:49:10.
 * Describe:
 */

public class BlurMaskActivity extends AppCompatActivity {
    private BlurMaskView mBlurMaskView;
    private Button mBtnInner,mBtnOuter,mBtnNormal,mBtnSolid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blur_mask);
        mBlurMaskView = findViewById(R.id.blur);
        mBtnInner = findViewById(R.id.inner);
        mBtnNormal = findViewById(R.id.normal);
        mBtnOuter = findViewById(R.id.outer);
        mBtnSolid = findViewById(R.id.solid);
        mBtnInner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBlurMaskView.setBlurType(0);
            }
        });

        mBtnSolid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBlurMaskView.setBlurType(1);
            }
        });

        mBtnNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBlurMaskView.setBlurType(2);
            }
        });

        mBtnOuter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBlurMaskView.setBlurType(3);

            }
        });
    }
}
