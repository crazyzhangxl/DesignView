package com.example.apple.designview.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 7/1/14.
 *
 * 一个比较完备的
 */
public class WheelView extends ScrollView {
    private int mIndicatorColor = Color.GREEN;
    private int mTextSelectColor = Color.BLACK;
    private int mTextNormalColor = Color.parseColor("#bbbbbb");
    private int mLineLength;
    private float mLineHeight = 1f;
    public static final String TAG = WheelView.class.getSimpleName();
    private int mPadding = 15;
    private int mTextSize = 20;

    /*———————————————————————————————————————————— 设置一些其他的有用的属性  开始——————————————————————————————————————————*/

    public WheelView setIndicatorColor(int color){
        mIndicatorColor = color;
        return this;
    }

    public WheelView setLineLength(int length){
        mLineLength = dip2px(length);
        return this;
    }

    public WheelView setLineHeight(float lineHeight){
        mLineHeight = lineHeight;
        return this;
    }

    public WheelView setTextSelectedColor(int color){
        mTextSelectColor = color;
        return this;
    }

    public WheelView setTextNormalColor(int color){
        mTextNormalColor = color;
        return this;
    }

    public WheelView setTextSize(int textSp){
        this.mTextSize = textSp;
        return this;
    }


    /**
     * 设置的偏移量 默认就是1
     * @param offset
     * @return
     */
    public WheelView setOffset(int offset) {
        this.offset = offset;
        return this;
    }

/*
    起初设置的选中 ————————————
    public WheelView setSelection(int position) {
        final int p = position;
        selectedIndex = p + offset;
        this.post(new Runnable() {
            @Override
            public void run() {
                //WheelView.this.smoothScrollTo(0, p * itemHeight);
                WheelView.this.scrollTo(0,p * itemHeight);
            }
        });
        return this;
    }*/



    public WheelView setItems(List<String> list) {
        if (null == items) {
            items = new ArrayList<String>();
        }
        items.clear();
        items.addAll(list);

        // 前面和后面补全
        for (int i = 0; i < offset; i++) {
            items.add(0, "");
            items.add("");
        }
        return this;
    }

    /**
     * 一定需要设置选中的字符串 ——————————————————
     * @param str
     * @return
     */
    public WheelView setSelectedString(String str){
        int x = -1;
        if (!TextUtils.isEmpty(str)){
            x = items.indexOf(str) - 2;
        }
        final int p = x + offset;
        this.post(new Runnable() {
            @Override
            public void run() {
                // 该方法含有滑动动画 ————————————
                //WheelView.this.smoothScrollTo(0, p * itemHeight);
                WheelView.this.scrollTo(0,p * itemHeight);
            }
        });
        return this;
    }


    public WheelView setTextPadding(int intPadding){
        this.mPadding = intPadding;
        return this;
    }



    /**
     * 必然调用 *********************
     */
    public void doInitEnd(){
        initData();
    }

    /*———————————————————————————————————————————— 设置一些其他的有用的属性  结束——————————————————————————————————————————*/



    public static class OnWheelViewListener {
        void onSelected(int selectedIndex, String item) {
        }
    }

    /**
     * 上下文
     */
    private Context context;

    /**
     * ScrollView 唯一子布局
     */
    private LinearLayout views;

    public WheelView(Context context) {
        super(context);
        init(context);
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    List<String> items;

    private List<String> getItems() {
        return items;
    }

    /**
     *
     */
    public static final int OFF_SET_DEFAULT = 1;

    /**
     * 偏移量（需要在最前面和最后面补全
     */
    int offset = OFF_SET_DEFAULT;

    public int getOffset() {
        return offset;
    }

    // 每页显示的数量
    int displayItemCount;

    int selectedIndex = 1;

    /**
     * 执行初始化 -------------------
     * @param context
     */
    private void init(Context context) {
        this.context = context;
        // 设置ScrollView的Bar取消
        this.setVerticalScrollBarEnabled(false);
        // 初始化一个线性布局 没有添加信息包嘛
        views = new LinearLayout(context);
        views.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                ,ViewGroup.LayoutParams.WRAP_CONTENT));
        views.setOrientation(LinearLayout.VERTICAL);
        this.addView(views);

        // 设置下划线的高度
        mLineLength = dip2px(60);
        // 滑动任务 ————————  核心代码 *******
        scrollerTask = new Runnable() {
            @Override
            public void run() {
                int newY = WheelView.this.getScrollY();
                if (initialY - newY == 0) {
                    // stopped
                    final int remainder = initialY % itemHeight;
                    final int divided = initialY / itemHeight;
                    if (remainder == 0) {
                        selectedIndex = divided + offset;
                        WheelView.this.onSelectedCallBack();
                    } else {
                        if (remainder > itemHeight / 2) {
                            WheelView.this.post(new Runnable() {
                                @Override
                                public void run() {
                                    WheelView.this.smoothScrollTo(0, initialY - remainder + itemHeight);
                                    selectedIndex = divided + offset + 1;
                                    WheelView.this.onSelectedCallBack();
                                }
                            });
                        } else {
                            WheelView.this.post(new Runnable() {
                                @Override
                                public void run() {
                                    WheelView.this.smoothScrollTo(0, initialY - remainder);
                                    selectedIndex = divided + offset;
                                    WheelView.this.onSelectedCallBack();
                                }
                            });
                        }
                    }
                } else {
                    initialY = WheelView.this.getScrollY();
                    WheelView.this.postDelayed(scrollerTask, newCheck);
                }
            }
        };
    }

    int initialY;
    Runnable scrollerTask;
    int newCheck = 50;

    public void startScrollerTask() {
        initialY = getScrollY();
        this.postDelayed(scrollerTask, newCheck);
    }

    private void initData() {
        displayItemCount = offset * 2 + 1;
        for (String item : items) {
            // TextView 添加到Linear布局中
            views.addView(createView(item));
        }
        refreshItemView(0);
    }

    int itemHeight = 0;

    /**
     *  根据文本信息 返回TextaView
     * @param item
     * @return
     */
    private TextView createView(String item) {
        TextView tv = new TextView(context);
        // 宽度铺满 高度自适应
        tv.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        // 设置单行
        tv.setSingleLine(true);
        // 设置字体大小
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSize);
        // 设置文本
        tv.setText(item);
        tv.setGravity(Gravity.CENTER);
        int padding = dip2px(mPadding);
        // 设置上下左右的间距 ——————————
        tv.setPadding(padding, padding, padding, padding);
        if (0 == itemHeight) {
            itemHeight = getViewMeasuredHeight(tv);
            views.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, itemHeight * displayItemCount));
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.getLayoutParams();
            this.setLayoutParams(new LinearLayout.LayoutParams(lp.width, itemHeight * displayItemCount));
        }
        return tv;
    }

    private void refreshItemView(int y) {
        int position = y / itemHeight + offset;
        int remainder = y % itemHeight;
        int divided = y / itemHeight;

        if (remainder == 0) {
            position = divided + offset;
        } else {
            if (remainder > itemHeight / 2) {
                position = divided + offset + 1;
            }
        }

        int childSize = views.getChildCount();
        for (int i = 0; i < childSize; i++) {
            TextView itemView = (TextView) views.getChildAt(i);
            if (null == itemView) {
                return;
            }
            if (position == i) {
                itemView.setTextColor(mTextSelectColor);
            } else {
                itemView.setTextColor(mTextNormalColor);
            }
        }
    }

    /**
     * 获取选中区域的边界
     */
    int[] selectedAreaBorder;

    private int[] obtainSelectedAreaBorder() {
        if (null == selectedAreaBorder) {
            selectedAreaBorder = new int[2];
            selectedAreaBorder[0] = itemHeight * offset;
            selectedAreaBorder[1] = itemHeight * (offset + 1);
        }
        return selectedAreaBorder;
    }

    private int scrollDirection = -1;
    private static final int SCROLL_DIRECTION_UP = 0;
    private static final int SCROLL_DIRECTION_DOWN = 1;

    Paint paint;
    int viewWidth;

    /**
     * 这里只会执行一次 ———————————— 只会绘制一次2条线段 ————————————————————
     * @param background
     */
    @Override
    public void setBackgroundDrawable(Drawable background) {
        Log.e(TAG,"----setBackgroundDrawable----");
        if (viewWidth == 0) {
            viewWidth = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
            Log.d(TAG, "viewWidth: " + viewWidth);
        }

        if (null == paint) {
            paint = new Paint();
            paint.setColor(mIndicatorColor);
            paint.setStrokeWidth(dip2px(mLineHeight));
        }

        background = new Drawable() {
            @Override
            public void draw(Canvas canvas) {
                canvas.drawLine(viewWidth / 2 - mLineLength/2, obtainSelectedAreaBorder()[0], viewWidth /2 +  mLineLength/2, obtainSelectedAreaBorder()[0], paint);
                canvas.drawLine(viewWidth / 2 - mLineLength/2, obtainSelectedAreaBorder()[1], viewWidth /2 +  mLineLength/2, obtainSelectedAreaBorder()[1], paint);
            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(ColorFilter cf) {

            }

            @Override
            public int getOpacity() {
                return 0;
            }
        };
        super.setBackgroundDrawable(background);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "w: " + w + ", h: " + h + ", oldw: " + oldw + ", oldh: " + oldh);
        viewWidth = w;
        Log.e(TAG,"----onSizeChanged----" + viewWidth);
        setBackgroundDrawable(null);
    }

    /**
     * scrollView滑动
     * @param l
     * @param t
     * @param oldl
     * @param oldt
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        Log.e(TAG, "onScrollChanged: ---------"+t);
        refreshItemView(t);
        if (t > oldt) {
            // 说明是往下滑动的
            scrollDirection = SCROLL_DIRECTION_DOWN;
        } else {
            scrollDirection = SCROLL_DIRECTION_UP;

        }
    }


    @Override
    public void fling(int velocityY) {
        super.fling(velocityY / 3);
        Log.e(TAG,"fling-----------------");
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            Log.e(TAG,"------------onTouchEvent UP-----------");
            startScrollerTask();
        }
        return super.onTouchEvent(ev);
    }




    /**
     * 选中回调
     */
    private void onSelectedCallBack() {
        if (null != onWheelViewListener) {
            onWheelViewListener.onSelected(selectedIndex, items.get(selectedIndex));
        }

    }



    public String getSeletedItem() {
        return items.get(selectedIndex);
    }

    public int getSeletedIndex() {
        return selectedIndex - offset;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private OnWheelViewListener onWheelViewListener;

    public OnWheelViewListener getOnWheelViewListener() {
        return onWheelViewListener;
    }

    public void setOnWheelViewListener(OnWheelViewListener onWheelViewListener) {
        this.onWheelViewListener = onWheelViewListener;
    }

    private int dip2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int getViewMeasuredHeight(View view) {
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        view.measure(width, expandSpec);
        return view.getMeasuredHeight();
    }
}
