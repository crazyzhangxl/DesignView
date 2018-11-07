package com.example.apple.designview.activitys.indicator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apple.designview.R;


/**
 * @author zxl on 2018/9/12.
 *         discription:
 */

public class FragmentA extends Fragment {
    private static final String TAG = "FragmentA";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_show_a, container, false);
        // 记住这里一定要返回,否则会出现空白界面的----
        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Log.e(TAG, "setUserVisibleHint: "+ isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
    }


}
