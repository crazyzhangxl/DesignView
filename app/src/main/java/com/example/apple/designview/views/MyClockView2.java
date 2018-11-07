package com.example.apple.designview.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.apple.designview.activitys.Utils.MeasureUtils;

/**
 * @author crazyZhangxl on 2018/10/24.
 * Describe:时钟测试
 *
 * 首先画圆,接着画背景,然后画圆点,接着画刻度线
 */
public class MyClockView2 extends View {

    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 300;

    // 边缘画笔
    private Paint mBoardPaint;
    private int mBoardColor = Color.BLACK;
    private int mBoardWidth = 10;

    // 填充画笔
    private Paint mSolidPaint;
    private int mSolidColor = Color.GREEN;


    private Point mPointCenter;
    private int mRadius;
    public MyClockView2(Context context) {
        this(context,null);
    }

    public MyClockView2(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public MyClockView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initClockView();
    }

    private void initClockView() {
        mBoardPaint = new Paint();
        mBoardPaint.setColor(mBoardColor);
        mBoardPaint.setStyle(Paint.Style.STROKE);
        mBoardPaint.setStrokeWidth(mBoardWidth);
        mBoardPaint.setAntiAlias(true);
        // 光滑过渡效果
        mBoardPaint.setDither(true);


        mSolidPaint = new Paint();
        mSolidPaint.setColor(mSolidColor);
        mSolidPaint.setStyle(Paint.Style.FILL);
        mSolidPaint.setAntiAlias(true);
        mSolidPaint.setDither(true);

         mPointCenter = new Point();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureUtils.measure(widthMeasureSpec,DEFAULT_WIDTH)
                ,MeasureUtils.measure(heightMeasureSpec,DEFAULT_HEIGHT));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int minLength = Math.min(w,h);
        mPointCenter.x = minLength/2;
        mPointCenter.y = minLength/2;
        mRadius = minLength/3;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBoardCircle(canvas);
        drawSolidCircle(canvas);
    }

    private void drawBoardCircle(Canvas canvas) {
        canvas.drawCircle(mPointCenter.x,mPointCenter.y,mRadius,mBoardPaint);
    }

    private void drawSolidCircle(Canvas canvas) {
        canvas.drawCircle(mPointCenter.x,
                mPointCenter.y,
                mRadius -  mBoardPaint.getStrokeWidth()/2,
                mSolidPaint);
    }
}
