package com.example.apple.designview.activitys;

import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.apple.designview.R;
import com.example.apple.designview.views.ProgressLineView;

/**
 * @author crazyZhangxl on 2018-11-28 16:38:49.
 * Describe:线性的进度线条
 */

public class ProgressLineActivity extends AppCompatActivity {
    private ProgressLineView mProgressLineView;
    private Button mBtnStart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_line);
        mProgressLineView = findViewById(R.id.progressLine);
        mBtnStart = findViewById(R.id.btnStart);
        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValueAnimator valueAnimator = ValueAnimator.ofInt(0,100);
                valueAnimator.setDuration(3000);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int animatedValue = (int) animation.getAnimatedValue();
                        mProgressLineView.setProgress(animatedValue);
                    }
                });
                valueAnimator.start();
            }
        });

    }
}
