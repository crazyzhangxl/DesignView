package com.example.apple.designview.views.bezier;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @author crazyZhangxl on 2018/11/9.
 * Describe: 一个简易的波浪线 ----
 *            目标,能够绘制出随布局文件改变的View
 *
 *            这里的长度---后面还要学习进行适配-----
 *            dp/sp ---> px 后面还要再稍微整理下呢
 */
public class BezierSampleView  extends View{
    private int mDefaultHeight = 200;
    private int mDefaultWidth = 500;
    private int mMeasuredHeight;
    private int mMeasureWidth;
    private Paint mWavePaint;
    // 水平方向上波浪线 距离左右的距离
    private int horizontalMarginLine = 20;
    // 数值方向上控制点距离上下的距离
    private int verticalMarginLine = 20;

    private Path mPath;


    public BezierSampleView(Context context) {
        this(context,null);
    }

    public BezierSampleView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public BezierSampleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaints();
    }

    /**
     * 加载完xml布局后回调 当然这里new 应该不回调用了的---
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.e("wava_view","onFinishInflate()-----");
    }

    private void initPaints() {
        mWavePaint = new Paint();
        //设置平滑过渡
        mWavePaint.setDither(true);
        // 设置抗锯齿
        mWavePaint.setAntiAlias(true);
        mWavePaint.setStrokeWidth(4f);
        mWavePaint.setStrokeCap(Paint.Cap.ROUND);
        mWavePaint.setColor(Color.BLACK);
        mWavePaint.setStyle(Paint.Style.STROKE);
        mPath = new Path();
    }

    /**
     * 当长宽为wrap_content时,那么为At_most模式;
     * 需要指定默认值,不然会转变为match_parent
     * @param widthMeasureSpec  宽度的测量尺寸
     * @param heightMeasureSpec 高度的测量尺寸
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int resultWidth = widthSize;
        int resultHeight = heightSize;
        if (MeasureSpec.AT_MOST == widthMode){
            resultWidth = mDefaultWidth;
        }

        if (MeasureSpec.AT_MOST == heightMode){
            resultHeight = mDefaultHeight;
        }

        setMeasuredDimension(resultWidth,resultHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMeasureWidth = w;
        mMeasuredHeight = h;
    }

    /**
     * 我就是想绘制一个 -- .        .         .
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float firstBaseX = horizontalMarginLine;
        float thirdBaseX = mMeasureWidth - horizontalMarginLine;
        float secondBaseX = (firstBaseX + thirdBaseX) / 2;
        float baseY = mMeasuredHeight / 2;
        float firstControlX =  (firstBaseX + secondBaseX) / 2;
        float secondControlX = (secondBaseX + thirdBaseX) / 2;
        float firstControlY =  verticalMarginLine;
        float secondControlY = mMeasuredHeight - verticalMarginLine;
        mPath.moveTo(firstBaseX,baseY);
        mPath.quadTo(firstControlX,firstControlY,secondBaseX,baseY);
        mPath.quadTo(secondControlX,secondControlY,thirdBaseX,baseY);
        canvas.drawPath(mPath,mWavePaint);
    }
}
