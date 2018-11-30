package com.example.apple.designview.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author crazyZhangxl on 2018/11/28.
 * Describe:
 */
public class ProgressLineView extends View{
    private int mDefaultHeight = 30;
    private int mDefaultLength = 300;
    private Paint mTextPaint;
    private Paint mLinePaint;
    private int mRealWidth;
    private int mRealHeight;
    private int progress;
    private String text;
    private int textLength;
    private int mTextPadding = 5;

    public ProgressLineView(Context context) {
        this(context,null);
    }

    public ProgressLineView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public ProgressLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs();
    }

    /**
     * 初始化属性
     */
    private void initAttrs() {
        float density = getResources().getDisplayMetrics().density;
        mDefaultLength = (int) (density * mDefaultLength);
        mDefaultHeight = (int) (density * mDefaultHeight);
        mTextPadding = (int)(density * mTextPadding);
        mTextPaint = new Paint();
        mLinePaint = new Paint();
        mTextPaint.setColor(Color.RED);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStrokeWidth(3);
        mTextPaint.setTextSize(30);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mLinePaint.setColor(Color.BLACK);
        mLinePaint.setStrokeWidth(3);
        mLinePaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int resultWidth = widthSize;
        int resultHeight = heightSize;
        if (MeasureSpec.AT_MOST == widthMode){
            resultWidth = mDefaultLength;
        }

        if (MeasureSpec.AT_MOST == heightMode){
            resultHeight = mDefaultHeight;
        }
        setMeasuredDimension(resultWidth,resultHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRealWidth = w;
        mRealHeight = h;
    }

    public void  setProgress(int progress){
        this.progress = progress;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        text = progress + "%";
        textLength = (int) mTextPaint.measureText(text,0,text.length());
        float value = (mRealWidth - mTextPadding * 2 - textLength) / 100f;
        canvas.drawLine(0,mRealHeight/2,progress*value,mRealHeight/2,mLinePaint);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float baseLine = mRealHeight/2 - (fontMetrics.top+fontMetrics.bottom)/2;
        canvas.drawText(text,progress*value+textLength/2+mTextPadding,baseLine,mTextPaint);
        canvas.drawLine(progress*value+mTextPadding*2+textLength,mRealHeight/2,mRealWidth,mRealHeight/2,mLinePaint);

    }
}
