package com.example.apple.designview.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.apple.designview.R;

/**
 * @author crazyZhangxl on 2018/11/30.
 * Describe:
 */
public class ShadowLayerView extends View{
    private Paint mPaint;
    private Bitmap mBitmap;
    private boolean mShadow = true;

    public ShadowLayerView(Context context) {
       this(context,null);
    }

    public ShadowLayerView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public ShadowLayerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayerType(LAYER_TYPE_SOFTWARE,null);
        init();
    }

    public void setShadow(boolean shadow){
        this.mShadow = shadow;
        invalidate();
    }

    private void init() {
       mPaint = new Paint();
       mPaint.setColor(Color.BLACK);
       mPaint.setStyle(Paint.Style.FILL);
       mPaint.setTextSize(30);
       mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mShadow){
            mPaint.setShadowLayer(1,10,10,Color.GRAY);
        }else {
            // 清除阴影
            mPaint.clearShadowLayer();
        }
        canvas.drawText("学习View",50,100,mPaint);
        canvas.drawCircle(150,200,50,mPaint);
        // drawBitmap这个API掌握的很一般啊
        canvas.drawBitmap(mBitmap,null,new Rect(300,200,300+mBitmap.getWidth(),200+mBitmap.getWidth()),mPaint);
    }
}
