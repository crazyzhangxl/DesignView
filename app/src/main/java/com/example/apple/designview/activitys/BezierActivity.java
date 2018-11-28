package com.example.apple.designview.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.example.apple.designview.R;
import com.example.apple.designview.views.bezier.BezierBoardView;

/**
 * @author crazyZhangxl on 2018-11-9 19:36:32.
 * Describe: 贝塞尔曲线学习  ------
 * 这里虽然之前有了解过，将实在的还是通过学习 启航大神的讲解，才大概有点概念，还需要好好学习，多多熟悉
 *
 * 主要是二阶
 */

public class BezierActivity extends AppCompatActivity {
    private BezierBoardView mBezierBoardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier);
        mBezierBoardView = findViewById(R.id.bezier);
    }
}
