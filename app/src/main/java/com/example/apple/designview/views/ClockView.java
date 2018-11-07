package com.example.apple.designview.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.apple.designview.activitys.Utils.MeasureUtils;

/**
 * @author crazyZhangxl on 2018/10/19.
 * Describe:  这倒不是圆形进度条 现在这是一个钟表罢了
 * 好的文章值得学习的 圆形进度条
 * https://www.jianshu.com/p/c2abd6226897
 * https://github.com/Lloyd0577/CustomClockForAndroid/blob/master/app/src/main/java/com/example/administrator/timeviewdemo/TimeView.java
 */
public class ClockView extends View{
    private Context mContext;
    private Paint mTextPaint;
    private Paint mCirclePaint;
    private final int DEFAULT_SZIE = 300;
    private int mTextSize = 30;
    // 圆心 ---
    private int mTextStrokeWidth = 10;
    private int mCircleStrokeWidth = 10;

    /**
     * 定义圆弧的起始角度和结束关闭的角度
     */
    private float mStartAngle, mSweepAngle;
    //圆心坐标，半径
    private Point mCenterPoint;
    private float mRadius;
    private float mBaseLineY;

    /**
     * 小时数文字与小时数刻度的距离
     */
    private float textDisHourTime = 30;

    /**
     * 小时刻度的长度
     */
    private float hourDimLength = 20;
    private String objectStr = "我在中间";
    public ClockView(Context context) {
        this(context,null);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initCircleProgress();
    }

    private void initCircleProgress() {
        mTextPaint = new Paint();
        mTextPaint.setColor(Color.RED);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setDither(true);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setStrokeWidth(mTextStrokeWidth);

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setDither(true);
        mCirclePaint.setStrokeWidth(mCircleStrokeWidth);

        mCenterPoint = new Point();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        setMeasuredDimension(MeasureUtils.measure(widthMeasureSpec,DEFAULT_SZIE),
                MeasureUtils.measure(heightMeasureSpec,DEFAULT_SZIE));
    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 计算圆心值
        mCenterPoint.x = w / 2;
        mCenterPoint.y = h / 2;
        mRadius =  w/2 - 50;
        mBaseLineY = mCenterPoint.y + MeasureUtils.getCenterDiastance(mTextPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 外圆边界
        mCirclePaint.setColor(Color.BLACK);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(mCenterPoint.x,mCenterPoint.y,mRadius,mCirclePaint);

        // 内圆背景
        mCirclePaint.setColor(Color.GREEN);
        mCirclePaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mCenterPoint.x,mCenterPoint.y,mRadius - mCircleStrokeWidth/2,mCirclePaint);

        // 绘制点
        canvas.drawPoint(mCenterPoint.x,mCenterPoint.y,mTextPaint);


        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(Color.BLACK);
        // 画刻度
        canvas.save();
        for (int i=0;i<360;i++){
            if (i % 30 ==0){
                mTextPaint.setStrokeWidth(5);
                canvas.drawLine(mCenterPoint.x, mCenterPoint.y-(mRadius - mCircleStrokeWidth/2)
                        ,mCenterPoint.x,mCenterPoint.y-(mRadius - mCircleStrokeWidth/2)+hourDimLength,mTextPaint);
            }
            canvas.rotate(1,mCenterPoint.x,mCenterPoint.y);
        }
        // 还原到save的状态
        canvas.restore();

        canvas.save();
        mTextPaint.setStrokeWidth(3);
        for (int i =0;i<12;i++){
            drawNum(i,canvas);
            canvas.rotate(30,mCenterPoint.x,mCenterPoint.y);
        }

        canvas.restore();
    }

    private void drawNum(int i,Canvas canvas) {
        String text;
        if (i == 0){
            text = String.valueOf(12);
        }else {
            text = String.valueOf(i);
        }

        canvas.drawText(text,mCenterPoint.x,
                mCenterPoint.y-(mRadius - mCircleStrokeWidth/2)+hourDimLength+textDisHourTime
                ,mTextPaint);
    }
}
