package com.example.apple.designview.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author crazyZhangxl on 2018/11/4.
 * Describe: 简易的钟表demo
 */
public class SampleClockView extends View {
    private int defaultHeight = 300;
    private int defaultWidth = 300;
    private Point mCenterPoint;

    //外边缘圆的半径
    private int mBorderRadius;

    //外边缘画笔
    private Paint mBorderPaint;

    //外圆颜色
    private int mBorderColor = Color.BLACK;

    //画笔宽度
    private int mBorderPaintWidth = 10;

    /**
     * 内圆画笔
     */
    private Paint mInsidePaint;
    // 内圆颜色
    private int mInsideColor = Color.GREEN;

    /**
     * 时钟圆点
     */
    private Paint mCenterPaint;

    private Paint mDegreePaint;

    /**
     * 时钟 小时刻度的长度 宽度
     */
    private int mHourDegreeWidth = 4;
    private int mHourDegreeLength = 24;
    /**
     * 时钟 秒刻度的长度 宽度
     */
    private int mMinuteDegreeWidth = 2;
    private int mMinuteDegreeLength = 14;

    private int mSecondDegreeWidth = 1;
    private int mSecondDegreeLength = 8;
    /**
     * 数字画笔
     */
    private Paint mTextPaint;
    // 文字与边缘的距离
    private int mTextDistanceBord = 60;
    public SampleClockView(Context context) {
        this(context,null);
    }

    public SampleClockView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public SampleClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        clockInit();
    }

    private void clockInit() {
        mCenterPoint = new Point();
        // 设置外圆的画笔属性---
        mBorderPaint = new Paint();
        mBorderPaint.setDither(true);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderPaintWidth);
        // 设置内圆的画笔
        mInsidePaint = new Paint();
        mInsidePaint.setDither(true);
        mInsidePaint.setAntiAlias(true);
        mInsidePaint.setStyle(Paint.Style.FILL);
        mInsidePaint.setColor(mInsideColor);
        // 设置中心点
        mCenterPaint = new Paint();
        mCenterPaint.setColor(Color.BLACK);
        mCenterPaint.setStyle(Paint.Style.FILL);
        mCenterPaint.setAntiAlias(true);
        mCenterPaint.setDither(true);
        mCenterPaint.setStrokeWidth(10);
        // 设置画笔闭合的形状
        mCenterPaint.setStrokeCap(Paint.Cap.ROUND);

        mDegreePaint = new Paint();
        mDegreePaint.setDither(true);
        mDegreePaint.setAntiAlias(true);
        mDegreePaint.setStyle(Paint.Style.FILL);
        mDegreePaint.setColor(Color.BLACK);

        mTextPaint = new Paint();
        mTextPaint.setDither(true);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(28);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(Color.BLACK);
    }

    /**
     * 构造函数xml结束后调用
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    /**
     * 对于at_most模式,取默认值
     * exactly unspecified 就默认可以了
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int resultWidth = widthSize;
        int resultHeight = heightSize;
        switch (widthMode){
                case MeasureSpec.AT_MOST:
                    resultWidth = defaultWidth;
                    break;
                default:
                    break;
        }

        switch (heightMode){
            case MeasureSpec.AT_MOST:
                resultHeight = defaultHeight;
                break;
                default:
                    break;
        }
        setMeasuredDimension(resultWidth,resultHeight);
    }

    /**
     * 尺寸发生改变后调用
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //设置中心点以及外圆半径
        mCenterPoint.x = w/2;
        mCenterPoint.y = h/2;
        mBorderRadius = Math.min(h,w)/2 - 20;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBordCircle(canvas);
        drawInsideBg(canvas);
        drawCenterRound(canvas);
        drawHourDegreeWithText(canvas);
        drawMinuteDegree(canvas);
        drawSecondDegree(canvas);
    }


    /**
     * 画外围圆
     * @param canvas
     */
    private void drawBordCircle(Canvas canvas) {
        canvas.drawCircle(mCenterPoint.x,mCenterPoint.y,mBorderRadius,mBorderPaint);
    }

    /**
     * 绘制内圆背景
     * 提示下:由于外圆有画笔宽度 在此基础上得减少画笔一半
     * @param canvas
     */
    private void drawInsideBg(Canvas canvas) {
        canvas.drawCircle(mCenterPoint.x,mCenterPoint.y,mBorderRadius-mBorderPaintWidth/2,mInsidePaint);
    }

    /**
     * 绘制中心圆点
     * @param canvas
     */
    private void drawCenterRound(Canvas canvas) {
        canvas.drawPoint(mCenterPoint.x,mCenterPoint.y,mCenterPaint);
    }

    /**
     * 绘制小时刻度以及文本数字
     * @param canvas
     */
    private void drawHourDegreeWithText(Canvas canvas) {
        canvas.save();
        mDegreePaint.setStrokeWidth(mHourDegreeWidth);
        int startY =  mCenterPoint.y-mBorderRadius+mBorderPaintWidth/2;
        for (int i=0;i<12;i++){
            String value = "";
            if (i == 0){
                value = String.valueOf(12);
            }else {
                value = String.valueOf(i);
            }
            canvas.drawText(value,mCenterPoint.x,startY+mTextDistanceBord,mTextPaint);
            canvas.drawLine(mCenterPoint.x,startY,mCenterPoint.x,startY+mHourDegreeLength,mDegreePaint);
            // 每次旋转30度
            canvas.rotate(30,mCenterPoint.x,mCenterPoint.y);
        }
        canvas.restore();
    }

    /**
     * 绘制分钟刻度
     * @param canvas
     */
    private void drawMinuteDegree(Canvas canvas) {
        canvas.save();
        mDegreePaint.setStrokeWidth(mMinuteDegreeWidth);
        int startY =  mCenterPoint.y-mBorderRadius+mBorderPaintWidth/2;
        for (int i=0;i<360;i++){
            if (i%30 != 0){
                if (i%6==0){
                    canvas.drawLine(mCenterPoint.x,startY,mCenterPoint.x,startY+mMinuteDegreeLength,mDegreePaint);
                }
            }
            canvas.rotate(1,mCenterPoint.x,mCenterPoint.y);
        }
        canvas.restore();
    }

    /**
     * 绘制秒刻度
     * @param canvas
     */
    private void drawSecondDegree(Canvas canvas) {
        canvas.save();
        mDegreePaint.setStrokeWidth(mSecondDegreeWidth);
        int startY =  mCenterPoint.y-mBorderRadius+mBorderPaintWidth/2;
        for (int i=0;i<360;i++){
            if (i%30 != 0 ){
                if (i%6 != 0) {
                    canvas.drawLine(mCenterPoint.x, startY, mCenterPoint.x, startY + mSecondDegreeLength, mDegreePaint);
                }
            }
            canvas.rotate(1,mCenterPoint.x,mCenterPoint.y);
        }
        canvas.restore();
    }

}
