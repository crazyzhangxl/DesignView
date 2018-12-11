package com.example.apple.designview.views;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.io.File;

/**
 * @author crazyZhangxl on 2018/11/30.
 * Describe: 发光的View
 */
public class BlurMaskView extends View{
    private Paint mPaint;
    private int mBlurType;

    public BlurMaskView(Context context) {
       this(context,null);
    }

    public BlurMaskView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public BlurMaskView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 禁用硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE,null);
        initPaint();
    }


    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);

    }

    public void setBlurType(int type){
        this.mBlurType = type;
        mPaint.reset();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
        switch (type){
            case 0:
                mPaint.setMaskFilter(new BlurMaskFilter(50, BlurMaskFilter.Blur.INNER));
                break;
            case 3:
                mPaint.setMaskFilter(new BlurMaskFilter(50, BlurMaskFilter.Blur.OUTER));
                break;
            case 2:
                mPaint.setMaskFilter(new BlurMaskFilter(50, BlurMaskFilter.Blur.NORMAL));
                break;
            case 1:
                mPaint.setMaskFilter(new BlurMaskFilter(50, BlurMaskFilter.Blur.SOLID));
                break;
                default:
                    break;
        }
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(150,150,80,mPaint);
    }
}
