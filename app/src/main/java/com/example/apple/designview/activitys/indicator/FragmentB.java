package com.example.apple.designview.activitys.indicator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.apple.designview.R;


/**
 * @author zxl on 2018/9/12.
 *         discription:
 */

public class FragmentB extends Fragment {

    private static final String TAG = "FragmentB";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_a, container, false);
        initViews(view);
        Log.e(TAG, "onCreateView: " );
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: ");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.e(TAG, "setUserVisibleHint: "+isVisibleToUser);
    }



    private void initViews(View view) {
        TextView tvContent = view.findViewById(R.id.content);
        tvContent.setText("哈哈,我牛逼了啊,嘻嘻");
    }
}
