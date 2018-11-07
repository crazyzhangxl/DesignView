package com.example.apple.designview.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author crazyZhangxl on 2018/11/3.
 * Describe: 以100,100 绘制2条相交线段,并以该点为中点绘制文字
 *
 *
 */
public class CircleCenterView extends View {
    private Paint mLinePaint;
    private Paint mTextPaint;

    private int defaultWidth = 200;
    private int defaultHeight = 200;

    private int realWidth;
    private int realHeight;

    /**
     * 在java代码中new 出来
     * @param context
     */
    public CircleCenterView(Context context) {
        this(context,null);
    }

    /**
     * 在xml布局中得倒引用
     * @param context
     * @param attrs
     */
    public CircleCenterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initNormal();
    }

    private void initNormal() {
        mLinePaint = new Paint();
        mTextPaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(2);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setColor(Color.RED);

        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setStrokeWidth(5);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setDither(true);
        mTextPaint.setTextSize(30);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int resultWidth;
        int resultHeight;
        switch (widthMode){
                case MeasureSpec.AT_MOST:
                    resultWidth = defaultWidth;
                    break;
                    default:
                        resultWidth = widthSize;
                        break;
        }

        switch (heightMode){
            case MeasureSpec.AT_MOST:
                resultHeight = defaultHeight;
                break;
                default:
                    resultHeight = heightSize;
        }

        setMeasuredDimension(resultWidth,resultHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        realHeight = h;
        realWidth = w;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLines(canvas);
        drawText(canvas);
    }

    private void drawLines(Canvas canvas) {
        canvas.drawLine(20,100,180,100,mLinePaint);
        canvas.drawLine(100,20,100,180,mLinePaint);
    }

    private void drawText(Canvas canvas) {
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        int baseLineY =  100 +  (int)(-1/2f*(fontMetrics.bottom+fontMetrics.top));
        canvas.drawText("hello girl",100,baseLineY,mTextPaint);

    }
}
