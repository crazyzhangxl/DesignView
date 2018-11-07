package com.example.apple.designview.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author crazyZhangxl on 2018/10/19.
 * Describe: 一般而言,(x,y)代表所绘制图形对应的矩形的左上角，
 *            drawText() x,y  y对应的为文字的基线
 *            绘制文字 getTextBounds() ----------  通过将文字放入矩形中获得其长宽,这还是很重要的
 */
public class BasicDrawView extends View {
    private Context mContext;
    private Paint mPaint;
    public BasicDrawView(Context context) {
        this(context,null);
    }

    public BasicDrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initBasicView();
    }

    private void initBasicView() {
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        // px
        mPaint.setStrokeWidth(2f);
        // 设置抗锯齿
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //drawText(canvas);
        drawTextByPoint(canvas);

    }

    /**
     * 目标实现给出一个点(100,100) 以其为文字中心绘制文字
     * @param canvas
     */
    private void drawTextByPoint(Canvas canvas) {
        int x = 300;
        int y = 300;
        mPaint.setColor(Color.BLACK);
        canvas.drawLine(x-100,y,x+100,y,mPaint);
        canvas.drawLine(x-100,y-100,x+100,y-100,mPaint);
        canvas.drawLine(x-100,y+100,x+100,y+100,mPaint);
        mPaint.setColor(Color.RED);
        mPaint.setTextSize(120f);
        Rect rect = new Rect();
        String objectStr = "中国好";
        mPaint.getTextBounds(objectStr,0,objectStr.length(),rect);
        int textX = x - rect.width()/2;
        int textY = y + rect.height()/2;
        Paint.FontMetrics metrics = mPaint.getFontMetrics();
        float lastY = y - (metrics.bottom+metrics.top)/2;

        canvas.drawText(objectStr,textX,lastY,mPaint);

    }

    private void drawText(Canvas canvas) {
        int basicX = 20;
        int basicY = 200;
        canvas.drawLine(basicX,basicY,basicX+400,basicY,mPaint);
        mPaint.setTextSize(120f);
        String objStr = "中国好";
//        Rect textRect = new Rect();
//        mPaint.getTextBounds(objStr,0,objStr.length(),textRect);
        canvas.drawText(objStr,basicX,basicY,mPaint);

        Paint.FontMetrics metrics = mPaint.getFontMetrics();
        float ascent = basicY + metrics.ascent;
        float descent = basicY + metrics.descent;

        mPaint.setColor(Color.GREEN);
        // 防止抖动 可以过渡均匀
        mPaint.setDither(true);
        canvas.drawLine(basicX,ascent,basicX+400,ascent,mPaint);

        // 感觉这个4格线绘制出来了之后 那么文字才显示出来的
        mPaint.setColor(Color.BLACK);
        canvas.drawLine(basicX,descent,basicX+400,descent,mPaint);

    }
}
