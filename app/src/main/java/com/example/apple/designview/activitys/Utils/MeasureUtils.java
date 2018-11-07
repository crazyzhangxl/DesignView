package com.example.apple.designview.activitys.Utils;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;

/**
 * @author crazyZhangxl on 2018/10/19.
 * Describe:
 */
public class MeasureUtils {

    public static int measure(int measureSpec,int defaultSize){
        int result = defaultSize;
        int mode = View.MeasureSpec.getMode(measureSpec);
        int size = View.MeasureSpec.getSize(measureSpec);
        if (View.MeasureSpec.EXACTLY == mode){
            result  = size;
        }else if (View.MeasureSpec.AT_MOST == mode){
            // 它想的是至少比这个大吧
            result = Math.max(result,size);
        }
        return result;
    }

    /**
     * dip 转换成px
     *
     * @param dip
     * @return
     */
    public static int dipToPx(Context context, float dip) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }

    // 根据中间值来计算baseline

    public static float getCenterDiastance(Paint paint){
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return -1 * (fontMetrics.ascent+fontMetrics.descent) / 2;
    }
}
