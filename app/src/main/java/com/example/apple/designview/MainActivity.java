package com.example.apple.designview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.apple.designview.activitys.BasicDraw2Activity;
import com.example.apple.designview.activitys.BasicDrawActivity;
import com.example.apple.designview.activitys.indicator.IndicatorDemo1Activity;
import com.example.apple.designview.activitys.indicator.IndicatorDemo2Activity;

/**
 * @author crazyZhangxl on 2018-10-19 8:35:15.
 * Describe:
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnBasic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BasicDrawActivity.class));
            }
        });

        findViewById(R.id.review).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BasicDraw2Activity.class));
            }
        });


        findViewById(R.id.indicator1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, IndicatorDemo1Activity.class));
            }
        });

        findViewById(R.id.indicator2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, IndicatorDemo2Activity.class));
            }
        });
    }
}
