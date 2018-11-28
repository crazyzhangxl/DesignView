package com.example.apple.designview.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * @author crazyZhangxl on 2018/11/21.
 * Describe:
 */
public class SwitchView_Super extends View {
    private static final int radius = 60;
    //TODO 中间矩形左上角和右下角的坐标
    private int leftTop_X, leftTop_Y, rightBottom_X, rightBottom_Y;
    //TODO 最右边圆的圆心坐标
    private int rightRadius_X;
    private int rightRadius_Y;
    private Paint mPaint;
    //TODO 最上方绘制圆的画笔
    private Paint up_Paint;
    //TODO 判断是否将要向右滑动
    private boolean isToRightMove = false;
    //TODO 判断是否将要向左滑动
    private boolean isToLeftMove = false;
    //TODO 手指按下的X坐标
    private float down_X;
    //TODO 向右滑动距离
    private float moveToRight_Instance = 0;
    //TODO 向左滑动距离
    private float moveTOLeft_Instance = 0;
    //TODO 滑动距离
    private float moveInstance = 0;
    //TODO 向右滑动的状态
    private boolean isRight = false;
    private String state = "最左端";
    private String click_state = "";
    private String slipfliter = "";
    //TODO 定义一张空白的图片
    private Bitmap whiteBitmap;
    //TODO 当前动画执行值
    private float currentValue;
    //TODO 设置滑动渐变
    private RadialGradient radialGradient;
    //TODO 是否是第一次点击左边圆
    private boolean isFirst = false;
    private ValueAnimator valueAnimator_right, valueAnimator_left;
    private SwitchListener switchListener;

    public SwitchView_Super(Context context) {
        super(context);
        init();
    }

    public SwitchView_Super(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SwitchView_Super(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#bfbfbf"));

        up_Paint = new Paint();
        up_Paint.setAntiAlias(true);
        up_Paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        up_Paint.setStyle(Paint.Style.FILL);
        up_Paint.setColor(Color.parseColor("#ffffff"));
        currentValue = 1;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //TODO 计算矩形左上角和右下角的坐标
        leftTop_X = getMeasuredWidth() / 3;
        leftTop_Y = getMeasuredHeight() / 2 - radius;
        rightBottom_Y = getMeasuredHeight() / 2 + radius;
        //TODO 矩形宽度设置成三倍的圆半径
        rightBottom_X = getMeasuredWidth() / 3 + radius * 3;
        //TODO 计算最右边圆的圆心坐标
        rightRadius_X = getMeasuredWidth() / 3 + radius * 3;
        rightRadius_Y = getMeasuredHeight() / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //TODO 画左边的圆
        canvas.drawCircle(leftTop_X, rightRadius_Y, radius, mPaint);
        //TODO 画中间的矩形
        // canvas.drawRect(leftTop_X, leftTop_Y, rightBottom_X, rightBottom_Y, mPaint);
        canvas.drawRect(leftTop_X, rightRadius_Y - radius, leftTop_X + radius * 2, rightRadius_Y + radius, mPaint);
        //TODO 画出最右边的圆
        canvas.drawCircle(leftTop_X + radius * 2, rightRadius_Y, radius, mPaint);

        //TODO 画出上方移动的圆
        canvas.drawCircle(leftTop_X + currentValue, rightRadius_Y, radius - 2, up_Paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //TODO 判断点击区域是否在最左侧白色的圆上-->初始位置
                if (event.getX() > leftTop_X - radius && event.getX() < leftTop_X + radius && event.getY() > rightRadius_Y - radius && event.getY() < rightRadius_Y + radius) {
                    down_X = event.getX();
                    isToRightMove = true;
                    isToLeftMove = false;
                }
                //TODO 判断点击区域是否在最右侧白色的圆上-->最终位置(选中状态)
                else if (event.getX() > leftTop_X + radius && event.getX() < rightBottom_X && event.getY() > rightRadius_Y - radius && event.getY() < rightRadius_Y + radius) {
                    down_X = event.getX();
                    isToLeftMove = true;
                    isToRightMove = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                //TODO 判断如果是点击右侧
                if (event.getX() > leftTop_X + radius && event.getX() < rightBottom_X && event.getY() > rightRadius_Y - radius && event.getY() < rightRadius_Y + radius) {
                    if (currentValue != 120.0) {
                        doAnimation_Right();
                        switchListener.open();
                        isFirst = true;
                    }
                } else if (event.getX() > leftTop_X - radius && event.getX() < leftTop_X + radius && event.getY() > rightRadius_Y - radius && event.getY() < rightRadius_Y + radius) {
                    if (isFirst) {
                        if (currentValue != -0.0) {
                            doAnimation_Left();
                            switchListener.close();
                        }
                    }
                }
                isToLeftMove = false;
                isToRightMove = false;
                break;
                default:
                    break;
        }
        postInvalidate();
        return true;
    }

    //TODO 设置点击开关按钮右侧的动画
    private void doAnimation_Right() {
        valueAnimator_right = ValueAnimator.ofFloat(moveInstance, radius * 2);
        valueAnimator_right.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentValue = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        //TODO 当滑块到最右端的时候改变背景颜色
        valueAnimator_right.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mPaint.setColor(Color.RED);
                postInvalidate();
            }
        });
        valueAnimator_right.setDuration(800);
        valueAnimator_right.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator_right.start();
    }

    //TODO 设置点击开关按钮左侧的动画
    private void doAnimation_Left() {
        valueAnimator_left = ValueAnimator.ofFloat(2 * radius + moveInstance, 0);
        valueAnimator_left.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentValue = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        //TODO 当滑块到最左端的时候改变背景颜色
        valueAnimator_left.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mPaint.setColor(Color.GRAY);
                postInvalidate();
            }
        });
        valueAnimator_left.setDuration(500);
        valueAnimator_left.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator_left.start();
    }

    public interface SwitchListener {
        void close();

        void open();
    }

    public void setSwitchListener(SwitchListener switchListener) {
        this.switchListener = switchListener;
    }
}

