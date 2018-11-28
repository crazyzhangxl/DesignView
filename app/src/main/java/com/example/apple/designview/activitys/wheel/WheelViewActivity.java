package com.example.apple.designview.activitys.wheel;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.apple.designview.R;
import com.example.apple.designview.views.WheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author crazyZhangxl on 2018-11-23 17:14:56.
 * Describe: 滑动的轮子View
 */

public class WheelViewActivity extends AppCompatActivity {
    private CustomDialog mHeightDialog;
    private View mHeightView;
    private String mSelelectedItem;
    private TextView mTextView;

    private List<String> mHeightString = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel_view);
        mTextView = findViewById(R.id.tvResult);
        mHeightString.clear();
        for (int i= 10;i<=80;i++){
            mHeightString.add(i+"cm");
        }

        findViewById(R.id.btnShow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHeightDialog();
            }
        });
    }

    private void  showHeightDialog(){
        mHeightView = View.inflate(this, R.layout.dialog_height_select, null);
        mHeightDialog = new CustomDialog(this, mHeightView,R.style.MyDialog);
        WheelView wv = mHeightDialog.findViewById(R.id.wheel_view_wv);
        // 默认选中 记得初始化一下哦
        wv.setLineLength(80)
                .setIndicatorColor(Color.RED)
                .setLineHeight(1.5f)
                .setItems(mHeightString)
                .setSelectedString(mTextView.getText().toString().trim())
                .setTextPadding(10)
                .doInitEnd();
        wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {

            public void onSelected(int selectedIndex, String item) {
                mSelelectedItem = item;
            }
        });

        mHeightDialog.findViewById(R.id.ensure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextView.setText(mSelelectedItem);
                mHeightDialog.dismiss();
            }
        });


        mHeightDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHeightDialog.dismiss();
            }
        });
        mHeightDialog.show();
    }
}
