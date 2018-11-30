package com.example.apple.designview.activitys.indicator;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author apple
 *
 * 这里的思想是很重要的: 和 tablayout一样TextView作为文本 而下面的指示器是绘制出来的
 *
 * 实现的是指示器是指定长度的,可选三角指示器
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
	private List<String> mTitleList;
	/**
	 * 标题栏数目
	 */
	private int mTabCount;
	/**
	 * 指示器颜色
	 */
	private int mIndicatorColor = COLOR_INDICATOR_COLOR;

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
	 * colorListDrawable 颜色选择
	 */
	private ColorStateList mTabTextColors;

	private int mTextSizePx = 32;

	private Paint mTextPaint;


	/**
	 * 当前的viewpager position
	 *
	 * 0 - 1 时   mCurrentPosition = 0 , mCurrentOffset 从 0f - 1f
	 * 1 - 0 时   mCurrentPosition = 0 , mCurrentOffset 从 1f - 0f
	 */
	private int mCurrentPosition;

	private float mCurrentOffset;

	private ViewPager mViewPager;

	/**
	 * 选中的档期啊你的Position
	 */
	private int mSelectedPosition;

	/**
	 * 记录指示器x轴与Y轴的距离
	 */
	private IndicatorLeftWithRight mIndicatorLeftWithRight;
	private boolean mIsDrawTriangle = true;

	//------- 白色的三角指示器效果
	private Path mPath;
	private Paint mTrianglePaint;
	private int mTriangleWidth = 14;
	private int mTriangleHeight = 6;

	/**
	 * 设置与ViewPager的绑定关系
	 * @param mViewPager 传入ViewPager
	 */

	public void setUpWithViewPager(ViewPager mViewPager){
		this.mViewPager = mViewPager;
		mViewPager.addOnPageChangeListener(new SimpleOnPageChangedListener());
		doInit();
	}

	@SuppressWarnings("ConstantConditions")
	private void doInit() {
		mTitleList = new ArrayList<>();
		// 初始化后面用到的记录指示器的左右坐标的类
		mIndicatorLeftWithRight = new IndicatorLeftWithRight();
		// 文字颜色选择器
		mTabTextColors = createColorStateList(NORMAL_TEXT_COLOR,SELECT_TEXT_COLOR);
		// 当前选中的下标
		mSelectedPosition = mViewPager.getCurrentItem();
		// 赋值标题个数
		mTabCount = mViewPager.getAdapter().getCount();
		// 将标题文本加入
		for (int i = 0;i<mTabCount;i++){
			mTitleList.add(mViewPager.getAdapter().getPageTitle(i).toString());
		}
		// 初始化文本标题
		generateTitleView();
		// 初始化各类画笔
		mTextPaint = new Paint();
		mTextPaint.setTextSize(mTextSizePx);
		mPaint = new Paint();
		// 设置仿抖动
		mPaint.setDither(true);
		// 设置抗锯齿
		mPaint.setAntiAlias(true);
		mPaint.setColor(mIndicatorColor);
		mPaint.setStrokeWidth(INDICATOR_LENGTH);

		// ----- 三角形
		mPath = new Path();
		mTrianglePaint = new Paint();
		mTrianglePaint.setAntiAlias(true);
		mTrianglePaint.setDither(true);
		mTrianglePaint.setColor(Color.WHITE);
		mTrianglePaint.setStyle(Paint.Style.FILL);

		float density = getResources().getDisplayMetrics().density;
		mTriangleWidth = (int)(density *  mTriangleWidth);
		mTriangleHeight = (int)(density * mTriangleHeight);
	}

	public SimpleViewPagerIndicator(Context context)
	{
		this(context, null);
	}

	public SimpleViewPagerIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 期望不执行绘制？ false 即执行onDraw() ; 这里使用dispatchDraw()来绘制,绘制相当于是背景
		setWillNotDraw(false);
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
	}


	/**
	 * 使用dispatch 在ViewGroup中
	 * 绘制的x点  === 移动的距离+设置下划线居中
	 * @param canvas
	 */
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.dispatchDraw(canvas);
		if (mIsDrawTriangle){
			initTriangle();
			canvas.drawPath(mPath,mTrianglePaint);
		}else {
			int paintWidth = (int) mPaint.getStrokeWidth() / 2;
			// 绘制指定长的指示器
			getIndicatorX(mCurrentPosition);
			float indicatorLeft = mIndicatorLeftWithRight.left;
			float indicatorRight = mIndicatorLeftWithRight.right;
			if (mCurrentOffset > 0f && mCurrentOffset < mTabCount -1){
				getIndicatorX(mCurrentPosition + 1);
				float nextIndicatorLeft = mIndicatorLeftWithRight.left;
				float nextIndicatorRight = mIndicatorLeftWithRight.right;
				indicatorLeft = indicatorLeft + (nextIndicatorLeft - indicatorLeft) * mCurrentOffset;
				indicatorRight = indicatorRight + (nextIndicatorRight - indicatorRight) * mCurrentOffset;
			}
			canvas.drawLine(indicatorLeft,mHeight - paintWidth,indicatorRight,mHeight - paintWidth,mPaint);
		}



	}

	/**
	 * 获得指示的X轴以及Y轴
	 * @return
	 */
	private void getIndicatorX(int childPosition){
		View child = getChildAt(childPosition);
		int childLeft = child.getLeft();
		int childWidth = child.getWidth();
		mIndicatorLeftWithRight.left = childLeft + childWidth/2f - underLineLength/2f;
		mIndicatorLeftWithRight.right =childLeft + childWidth/2f + underLineLength/2f;
	}

	private class IndicatorLeftWithRight{
		float left;
		float right;
	}



	/**
	 * 准备绘制白色的三角形指示器
	 */
	private void initTriangle() {
		float centerX = getTriangleCenterX(mCurrentPosition);
		if (mCurrentPosition < mTabCount-1){
			float nextCenterX = getTriangleCenterX(mCurrentPosition + 1);
			centerX = centerX + (nextCenterX - centerX) * mCurrentOffset;
		}

		mPath = new Path();
		mPath.moveTo(centerX - mTriangleWidth/2, mHeight);
		mPath.lineTo(centerX + mTriangleWidth/2, mHeight);
		mPath.lineTo(centerX, mHeight - mTriangleHeight);
		mPath.close();
	}

	/**
	 * 获得文本的中点
	 * @param position
	 */
	private float  getTriangleCenterX(int position){
		View child = getChildAt(position);
		int left = child.getLeft();
		int width = child.getWidth();
		return left + width/2f;
	}




	/**
	 * 初始化文本编辑框 并设置点击事件
	 */
	private void generateTitleView()
	{
		if (getChildCount() > 0) {
			this.removeAllViews();
		}

		for (int i = 0; i < mTabCount; i++) {
			TextView tv = new TextView(getContext());
			LayoutParams lp = new LayoutParams(0,
					LayoutParams.MATCH_PARENT,1);
			tv.setGravity(Gravity.CENTER);
			tv.setText(mTitleList.get(i));
			tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSizePx);
			tv.setLayoutParams(lp);
			tv.setOnClickListener(new MyOnClickListener(i));
			// 设置颜色选择
			tv.setTextColor(mTabTextColors);
			// 设置默认选中第一个
			if (i == mSelectedPosition ){
				// 初始化操作
				tv.setSelected(true);
			}
			addView(tv);
		}
	}

	class MyOnClickListener implements View.OnClickListener{
		private int mIndex;

		public MyOnClickListener(int index){
			mIndex = index;
		}

		@Override
		public void onClick(View v) {
			mViewPager.setCurrentItem(mIndex);
		}
	}


	public void setIndicatorColor(int indicatorColor) {
		this.mIndicatorColor = indicatorColor;
	}



	/**
	 * 设置选中的View
	 * @param position
	 */
	public void setSelectedView(int position) {
		if (position < mTabCount) {
			for (int i = 0; i < mTabCount; i++) {
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

	private class SimpleOnPageChangedListener implements ViewPager.OnPageChangeListener{

		@Override
		public void onPageScrolled(int i, float v, int i1) {
			mCurrentPosition = i;
			mCurrentOffset = v;
			invalidate();
		}

		@Override
		public void onPageSelected(int i) {
			mSelectedPosition = i;
			setSelectedView(i);
		}

		@Override
		public void onPageScrollStateChanged(int i) {

		}
	}

	public SimpleViewPagerIndicator setIndicatorLinear(boolean isDrawTriangle){
		this.mIsDrawTriangle = isDrawTriangle;
		return this;
	}

}
