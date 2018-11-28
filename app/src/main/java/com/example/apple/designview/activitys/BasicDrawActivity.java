package com.example.apple.designview.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.apple.designview.R;
import com.example.apple.designview.views.JumpTextView;

/**
 * @author crazyZhangxl on 2018-10-19 8:37:37.
 * Describe:
 */

public class BasicDrawActivity extends AppCompatActivity {
    private JumpTextView mJumpTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_draw);
        mJumpTextView =  findViewById(R.id.jumpView);
        mJumpTextView.setDrawingState(false);
        mJumpTextView.setMyViewClickListener(new JumpTextView.MyViewClickListener() {

            @Override
            public void onJumpOnclick() {
                Toast.makeText(BasicDrawActivity.this, "点了", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(BasicDrawActivity.this,BasicDraw2Activity.class));
            }

            @Override
            public void onJumpEnd() {
                Toast.makeText(BasicDrawActivity.this, "结束啦", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(BasicDrawActivity.this,BasicDraw2Activity.class));
            }
        });

        findViewById(R.id.btnStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mJumpTextView.setDrawingState(true);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mJumpTextView.isProgressRunning){
            return;
        }
        super.onBackPressed();
    }
}
