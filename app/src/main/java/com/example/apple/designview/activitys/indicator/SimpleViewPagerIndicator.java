package com.example.apple.designview.activitys.indicator;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author apple
 *
 * 这里的思想是很重要的: 和 tablayout一样TextView作为文本 而下面的指示器是绘制出来的
 *
 * 这个是更加不错的哦
 */
public class SimpleViewPagerIndicator extends LinearLayout
{

	/**
	 * 指示器默认颜色
	 */
	private static final int COLOR_INDICATOR_COLOR = Color.GREEN;
	/**
	 * 未选中的默认文本颜色
	 */
	private static final int NORMAL_TEXT_COLOR = Color.RED;
	/**
	 * 选中的默认文本颜色
	 */
	private static final int SELECT_TEXT_COLOR = Color.GREEN;

	private static final float INDICATOR_LENGTH = 6.0F;
	/**
	 * 标题栏文字字符数组
	 */
	private String[] mTitles;
	/**
	 * 标题栏数目
	 */
	private int mTabCount;
	/**
	 * 指示器颜色
	 */
	private int mIndicatorColor = COLOR_INDICATOR_COLOR;
	/**
	 * 标注通过ViewPager onPageScrolled 滑动的数值
	 */
	private float mTranslationX;
	private Paint mPaint;
	/**
	 * 标注识别自定义View的高度,宽度
	 */
	private int mHeight,mWidth;
	/**
	 * 下划线指示器的默认长度
	 */
	private int underLineLength = 100;
	/**
	 * 标示当前指示器切换到居中时的偏移量
	 */
	private int offsetLength;
	/**
	 * 点击监听
	 */
	private onTextSelectedListener mOnTextSelectedListener;
	/**
	 * colorListDrawable 颜色选择
	 */
	private ColorStateList mTabTextColors;

	public SimpleViewPagerIndicator(Context context)
	{
		this(context, null);
	}

	public SimpleViewPagerIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 期望不执行绘制？ false 即执行onDraw() ; 这里使用dispatchDraw()来绘制,绘制相当于是背景
		// setWillNotDraw(false);
		mPaint = new Paint();
		// 设置仿抖动
		mPaint.setDither(true);
		// 设置抗锯齿
		mPaint.setAntiAlias(true);
		mPaint.setColor(mIndicatorColor);
		mPaint.setStrokeWidth(INDICATOR_LENGTH);
		mTabTextColors = createColorStateList(NORMAL_TEXT_COLOR,SELECT_TEXT_COLOR);
	}

	/**
	 * onSizeChanged 会在onMeasure之后调用 且位于onDraw()之前
	 * 在这里可以获得测量的高度长度等
	 * @param w
	 * @param h
	 * @param oldw
	 * @param oldh
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
		mHeight = h;
		mWidth = w;
		offsetLength = w / mTabCount /2 - underLineLength/2;
	}


	/**
	 * 使用dispatch 在ViewGroup中
	 * 绘制的x点  === 移动的距离+设置下划线居中
	 * @param canvas
	 */
	@Override
	protected void dispatchDraw(Canvas canvas)
	{
		super.dispatchDraw(canvas);
		int paintwidth = (int) mPaint.getStrokeWidth() / 2;
		canvas.drawLine(mTranslationX+offsetLength, mHeight-paintwidth, mTranslationX+offsetLength+underLineLength, mHeight-paintwidth, mPaint);
	}


	/**
	 * 移动ViewPager滑动的距离 相对于左边距而言 -----
	 * @param position
	 * @param offset
	 */
	public void scroll(int position, float offset)
	{
		/**
		 * <pre>
		 *  0-1:position=0 ;1-0:postion=0;
		 * </pre>
		 */
		mTranslationX = getWidth() / mTabCount * (position + offset);
		invalidate();
	}

	/**
	 * 设置文本 并设置相应的点击事件
	 * @param selectedItem
	 */
	private void generateTitleView(int selectedItem)
	{
		if (getChildCount() > 0) {
			this.removeAllViews();
		}
		int count = mTitles.length;
		setWeightSum(count);
		for (int i = 0; i < count; i++) {
			TextView tv = new TextView(getContext());
			LayoutParams lp = new LayoutParams(0,
					LayoutParams.MATCH_PARENT);
			// 设置权重
			lp.weight = 1;
			tv.setGravity(Gravity.CENTER);
			tv.setText(mTitles[i]);
			tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			tv.setLayoutParams(lp);
			tv.setOnClickListener(new MyOnClickListener(i));
			// 设置颜色选择
			tv.setTextColor(mTabTextColors);
			// 设置默认选中第一个
			if (i == selectedItem ){
				// 初始化操作
				tv.setSelected(true);
			}
			addView(tv);
		}
		mTranslationX = selectedItem * mWidth /count;
	}

	class MyOnClickListener implements View.OnClickListener{
		private int mIndex;

		public MyOnClickListener(int index){
			mIndex = index;
		}

		@Override
		public void onClick(View v) {
			setSelectedView(mIndex);
			if (mOnTextSelectedListener != null){
				mOnTextSelectedListener.onTextSelected(mIndex);
			}
		}
	}

	/**
	 * 设置标题和选中的哪一个
	 * @param titles
	 * @param selectedItem
	 */
	public void setTitles(String[] titles,int selectedItem) {
		mTitles = titles;
		mTabCount = titles.length;
		generateTitleView(selectedItem);
	}

	public void setIndicatorColor(int indicatorColor) {
		this.mIndicatorColor = indicatorColor;
	}

	/**
	 * 设置回调的点击事件 ------
	 * @param onTextSelectedListener
	 */
	public void setOnTextSelectedListener(onTextSelectedListener onTextSelectedListener){
		this.mOnTextSelectedListener = onTextSelectedListener;
	}

	interface onTextSelectedListener{
		void onTextSelected(int position);
	}

	/**
	 * 设置选中的View
	 * @param position
	 */
	public void setSelectedView(int position) {
		final int tabCount = getChildCount();
		if (position < tabCount) {
			for (int i = 0; i < tabCount; i++) {
				final TextView child = (TextView)getChildAt(i);
				child.setSelected(i == position);
			}
		}
	}

	/**
	 * 设置颜色选择
	 * @param defaultColor  默认颜色
	 * @param selectedColor 选中颜色
	 * @return
	 */
	private  ColorStateList createColorStateList(int defaultColor, int selectedColor) {
		final int[][] states = new int[2][];
		final int[] colors = new int[2];
		int i = 0;

		states[i] = SELECTED_STATE_SET;
		colors[i] = selectedColor;
		i++;

		// Default enabled state
		states[i] = EMPTY_STATE_SET;
		colors[i] = defaultColor;
		i++;
		return new ColorStateList(states, colors);
	}
}
