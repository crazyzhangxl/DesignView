package com.example.apple.designview.views.bezier;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author crazyZhangxl on 2018/11/9.
 * Describe: 贝塞尔画板 ----- 首先实现的是普通的画板绘制效果
 *
 * 提醒一下: 对于绘制方面的,不能使用绝对距离 getRawX() getRawY() 等
 *          因为Path路径是相当于控件左上角的 而用绝对距离会偏移---- 这个得注意了
 */
public class BezierBoardView  extends View{
    private int  mDefaultHeight = 400;
    private int mDefaultWidth = 600;
    private int mMeasureWidth;
    private int mMeasureHeight;
    private Paint mDrawPaint;
    private Path mDrawPath;
    private int mPaintWidth = 6;
    private float mBaseX;
    private float mBaseY;


    public BezierBoardView(Context context) {
        this(context,null);
    }

    public BezierBoardView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public BezierBoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.e("画板","onFinishInflate -----");
        initPaintWithPath();
    }

    private void initPaintWithPath() {
        mDrawPaint = new Paint();
        mDrawPaint.setColor(Color.BLACK);
        mDrawPaint.setStrokeWidth(mPaintWidth);
        mDrawPaint.setDither(true);
        mDrawPaint.setAntiAlias(true);
        mDrawPaint.setStrokeCap(Paint.Cap.ROUND);
        mDrawPaint.setStyle(Paint.Style.STROKE);

        mDrawPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e("画板","onMeasure -----");
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
        Log.e("画板","onSizeChanged -----");
        mMeasureHeight = h;
        mMeasureWidth = w;
    }

    /**
     * 路线里面不能使用 getRawX()
     * 改装为贝塞翁曲线
     *
     * 得探究为啥为down 为true才可以
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mBaseX = event.getX();
                mBaseY = event.getY();
                mDrawPath.moveTo(mBaseX, mBaseY);
                // 这里很的不知道为啥了---- 明天探究一下
                return true;
            case MotionEvent.ACTION_MOVE:
                float endX = event.getX();
                float endY = event.getY();
                float baseSecondX = (endX + mBaseX)/2;
                float baseScondY = (endY + mBaseY)/2;
                mDrawPath.quadTo(mBaseX,mBaseY,baseSecondX,baseScondY);
                mBaseX = endX;
                mBaseY = endY;
                // 每移动一下就要重新绘制
                invalidate();
                break;
                default:
                    break;
        }
        Log.e("画板","onTouchEvent -----");
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("画板","onDraw -----");
        canvas.drawColor(Color.RED);
        canvas.drawPath(mDrawPath,mDrawPaint);
    }

}
