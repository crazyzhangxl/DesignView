package com.example.apple.designview.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author crazyZhangxl on 2018/11/7.
 * Describe:
 */
public class UnderLineView extends View {
    private Paint mPaint;
    private int mViewHeight;
    public UnderLineView(Context context) {
        this(context,null);
    }

    public UnderLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initUnderLine();
    }

    private void initUnderLine() {
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(9f);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewHeight = h;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 对于平移而言 可以看作是移动的坐标系 --
     * 比如下面的效果是: 先绘制一条线段; 紧着者移动坐标轴 右下方移动,以移动后的坐标轴绘制线条 绘制完了之后还原
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLine(0,0,100,0,mPaint);
        canvas.save();
        canvas.translate(20, mViewHeight-10);
        canvas.drawLine(0, 0, 100, 0, mPaint);
        canvas.restore();
    }


}
