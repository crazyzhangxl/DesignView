package com.example.apple.designview.views.nice_indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author crazyZhangxl on 2018/11/12.
 * Describe: 自定义指示器
 *               1.支持指示器与文字等长效果 ******;
 *               2.支持指示器与指示器文本等长
 *               3.支持选中调整文字的大小
 *               和TabLayout效果相类似
 * 注意点:
 * 确保一定在ViewPagerAdapter中返回了:标题字符串,否则 <addTextFromViewPager/> 会报空指针异常
 */
public class NiceViewPagerIndicator extends HorizontalScrollView{

    public  enum IndicatorType{
        /**
         * 与标题等长
         */
        EQUAL_TAB,
        /**
         * 与文字等长
         */
        EQUAL_TEXT,
        /**
         * 指示器绝对长度
         */
        ABSOLUTE_LENGTH;
    }

    /**
     * 记录当前指示器的左右的X轴坐标
     */
    private float mLineLeft;
    private float mLineRight;

    private  NicePageChangeListener mNicePageChangeListener;
    private ViewPager mViewPager;
    private Context mContext;
    private IndicatorType mIndicatorType = IndicatorType.EQUAL_TEXT;
    /**
     * ScrollView下的唯一子布局
     * 承载容纳文本控件的作用 -----
     */
    private LinearLayout mLinearContainer;
    /**
     * 滑动过程中的选中操作指示器Index
     */
    private int mSelectedIndex;
    /**
     * 滑动结束后停留的指示器Index
     */
    private int mCurrentIndex;
    /**
     * ViewPager 滑动参数 0-1
     */
    private float mCurrentPositionOffset;
    /**
     * 标示tab的个数
     */
    private int mTabCount;

    /**
     * 默认控制器水平padding
     */
    private static final int DEFAULT_TAB_HNL_PADDING = 20;
    /**
     * 水平方向上的文本控件Padding
     */
    private int mTabHorizontalPadding = DEFAULT_TAB_HNL_PADDING;

    /**
     *  根据控件的多少设定是否需要等分:
     *  false ----- 设置标题平分(针对标题较 少)  [标题不可滑动]
     *  true  ----- 设置标题按一定的Padding铺满整个Linear 适应较多的标题 [标题可能可以滑动]
     */

    private boolean isExpand = false;
    private LinearLayout.LayoutParams wrapTabLayoutParams;
    private LinearLayout.LayoutParams expandTabLayoutParams;
    /*
     * 指示器（被选中的tab下的短横线）
     * true：indicator与文字等长；false：indicator与整个tab等长
     */

    private int mNormalTextColor = Color.BLACK;
    private int mSelectedTextColor = Color.RED;
    private int mNormalTextSize = 16;
    private int mSelectedTextSize = 16;
    /**
     * 标识 HorizontalScrollView滑动的x点
     */
    private int lastScrollX = 0;

    /**
     * 用于测量文字长度的画笔
     */
    private Paint mMeasureTextPaint;

    private Paint mIndicatorPaint;
    private int mIndicatorColor = Color.GREEN;

    private int mIndicatorStrokeWidth = 10;

    /**
     * 在ABSOLUTE_LENGTH 指示器类型下,默认的指示器长度
     */
    private int mIndicatorLength = 40;

    /*
     * scrollView整体滚动的偏移量,dp
     */
    private int mScrollOffset = 100;
    public NiceViewPagerIndicator(Context context) {
        this(context,null);
    }

    public NiceViewPagerIndicator(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public NiceViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 初始化ViewPager监听
        mNicePageChangeListener = new NicePageChangeListener();
        mContext = context;
        setFillViewport(true);
        // 允许ViewGroup执行onDraw()执行绘制操作 ---
        setWillNotDraw(false);
    }

    public void setUpViewPager(@NonNull ViewPager viewPager){
        this.mViewPager = viewPager;
        // 在adapter为空时抛出异常 解决后面报的空指针检查
        if (mViewPager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        viewPager.addOnPageChangeListener(mNicePageChangeListener);
        init();
        initViews();
    }

    /**
     * 进行初始化
     */
    private void init() {
        float density = getResources().getDisplayMetrics().density;
        mSelectedTextSize = (int)(mSelectedTextSize * density);
        mNormalTextSize = (int)(mNormalTextSize * density);
        mTabHorizontalPadding = (int)(mTabHorizontalPadding * density);
        mScrollOffset = (int)(mScrollOffset * density);
        mIndicatorLength =(int)(mIndicatorLength * density);
        addOnlyContainerChild();
        defTextIndicatorParams();
        initMeasureTextPaints();
        initIndicatorPaints();
    }

    private void initMeasureTextPaints() {
        /*
         * 文字画笔
         */
        mMeasureTextPaint = new Paint();
        mMeasureTextPaint.setAntiAlias(true);
        mMeasureTextPaint.setStrokeWidth(mIndicatorStrokeWidth);
        mMeasureTextPaint.setTextSize(mSelectedTextSize);
    }

    private void initIndicatorPaints() {
        mIndicatorPaint = new Paint();
        mIndicatorPaint.setColor(mIndicatorColor);
        mIndicatorPaint.setStrokeCap(Paint.Cap.ROUND);
        mIndicatorPaint.setStrokeWidth(mIndicatorStrokeWidth);
        mIndicatorPaint.setAntiAlias(true);
        mIndicatorPaint.setDither(true);
        mIndicatorPaint.setStyle(Paint.Style.FILL);
    }


    /**
     * 添加Horizontal 唯一子布局
     */
    private void addOnlyContainerChild() {
        mLinearContainer = new LinearLayout(mContext);
        mLinearContainer.setOrientation(LinearLayout.HORIZONTAL);
        mLinearContainer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        // 将唯一子布局添加上去
        addView(mLinearContainer);
    }

    /**
     * 创建两个Tab的LayoutParams，wrapTabLayoutParams     ---  为宽度包裹内容，控件较多
     *                          expandTabLayoutParams   ---  宽度等分父控件剩余空间,控件较少
     */
    private void defTextIndicatorParams() {
        //宽度包裹内容
        wrapTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        //宽度等分 这里用途会更加广泛一点----
        expandTabLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);
    }

    private void initViews() {
        mSelectedIndex = mViewPager.getCurrentItem();
        mCurrentIndex = mViewPager.getCurrentItem();
        if (mViewPager.getAdapter() != null) {
            mTabCount = mViewPager.getAdapter().getCount();
        }
        addTextFromViewPager();

        //滚动scrollView
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                // 绘制好了完成滑动------
                scrollToChild(mCurrentIndex, 0);
            }
        });
    }

    /**
     * 将从绑定关系的ViewPager的文本属性,添加到linear_container布局中去
     *
     * 跳过编译条件,满足下面的条件
     */
    @SuppressWarnings("ConstantConditions")
    private void addTextFromViewPager(){
        mLinearContainer.removeAllViews();
        for (int i=0 ;i< mTabCount ;i++){
            addTextTab(i, mViewPager.getAdapter().getPageTitle(i).toString());
        }
        updateTextTabStyle();
    }

    /**
     * 按照选中的更新标题式样
     */
    private void updateTextTabStyle() {
        for (int i=0;i<mTabCount;i++){
            TextView textView = (TextView) mLinearContainer.getChildAt(i);
            if ( i == mSelectedIndex){
                // 以px传递
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,mSelectedTextSize);
                textView.setTextColor(mSelectedTextColor);
            }else {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,mNormalTextSize);
                textView.setTextColor(mNormalTextColor);
            }
        }
    }

    /**
     * 添加文本Tab
     * @param i
     * @param pageTitle
     */
    private void addTextTab(int i, String pageTitle) {
        TextView textTab = new TextView(mContext);
        textTab.setGravity(Gravity.CENTER);
        textTab.setText(pageTitle);
        // 暂时定为红色
        textTab.setTextColor(mNormalTextColor);
        textTab.setTextSize(mNormalTextSize);
        // 设置左右的Padding
        textTab.setPadding(mTabHorizontalPadding,0,mTabHorizontalPadding,0);
        textTab.setOnClickListener(new TextClickSelectListener(i));
        // 设置文本控件的布局params方式
        mLinearContainer.addView(textTab,isExpand?expandTabLayoutParams:wrapTabLayoutParams);
    }

    /**
     * 绘制指示器 ----- 计算指示器的起点与中点进行绘制
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mTabCount == 0){
            return;
        }
        // 获得控件的高度
        final int height = getHeight();
        float nextLeft;
        float nextRight;
        // 依据文字等长还是tab等长进行效果展示
        switch (mIndicatorType){
            case EQUAL_TAB:
                View currentTab = mLinearContainer.getChildAt(mCurrentIndex);
                mLineLeft = currentTab.getLeft();
                mLineRight = currentTab.getRight();
                if (mCurrentPositionOffset > 0f && mCurrentIndex < mTabCount - 1) {
                    View nextTab = mLinearContainer.getChildAt(mCurrentIndex + 1);
                    nextLeft = nextTab.getLeft();
                    nextRight = nextTab.getRight();
                    mLineLeft = mLineLeft + (nextLeft - mLineLeft) * mCurrentPositionOffset;
                    mLineRight = mLineRight + (nextRight - mLineRight) * mCurrentPositionOffset;
                }
                break;
            case EQUAL_TEXT:
                getTextLocation(mCurrentIndex);
                mLineLeft = textLocation.left;
                mLineRight = textLocation.right;
                if (mCurrentPositionOffset > 0f && mCurrentIndex < mTabCount - 1) {
                    getTextLocation(mCurrentIndex + 1);
                    nextLeft = textLocation.left;
                    nextRight = textLocation.right;
                    mLineLeft = mLineLeft + (nextLeft - mLineLeft) * mCurrentPositionOffset;
                    mLineRight = mLineRight + (nextRight - mLineRight) * mCurrentPositionOffset;
                }
                break;
            case ABSOLUTE_LENGTH:
                getLinearLayoutWithLength(mCurrentIndex);
                mLineLeft = textLocation.left;
                mLineRight = textLocation.right;
                if (mCurrentPositionOffset > 0f && mCurrentIndex < mTabCount-1){
                    getLinearLayoutWithLength(mCurrentIndex + 1);
                    nextLeft = textLocation.left;
                    nextRight = textLocation.right;
                    mLineLeft = mLineLeft + (nextLeft - mLineLeft) * mCurrentPositionOffset;
                    mLineRight = mLineRight + (nextRight - mLineRight) * mCurrentPositionOffset;
                }
                break;
                default:break;
        }
        canvas.drawRect(mLineLeft, height - mIndicatorPaint.getStrokeWidth()/2, mLineRight, height, mIndicatorPaint);
    }

    /**
     * 获得指定tab中，文字的left和right
     */
    @SuppressWarnings("ConstantConditions")
    private void getTextLocation(int position) {
        View tab = mLinearContainer.getChildAt(position);
        String tabText = mViewPager.getAdapter().getPageTitle(position).toString();
        float textWidth = mMeasureTextPaint.measureText(tabText);
        int tabWidth = tab.getWidth();
        textLocation.left = tab.getLeft() + (int) ((tabWidth - textWidth) / 2);
        textLocation.right = tab.getRight() - (int) ((tabWidth - textWidth) / 2);
    }

    /**
     * 根据定长绘制指示器
     * @param position
     */
    private void getLinearLayoutWithLength(int position){
        View child = mLinearContainer.getChildAt(position);
        int childLeft = child.getLeft();
        int childWidth = child.getWidth();
        textLocation.left = (int)(childLeft + childWidth / 2f - mIndicatorLength/2f);
        textLocation.right = (int)(childLeft + childWidth / 2f + mIndicatorLength/2f);
    }

    private LeftRight textLocation = new LeftRight();

    class LeftRight {
        int left, right;
    }


    /**
     * 重写onClickListener,为了能够安全的将参数传递进来
     */
    private class TextClickSelectListener implements View.OnClickListener{
        private  int selectedIndex;

        TextClickSelectListener(int selectedIndex) {
            this.selectedIndex = selectedIndex;
        }

        @Override
        public void onClick(View v) {
            // 设置文本的选中----
            if (mViewPager != null){
                mViewPager.setCurrentItem(selectedIndex);
            }
        }
    }


    /**
     * 重写ViewPager的onPageChangeListener 目的在于监听滑动状态,进行绑定
     */
    private class NicePageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int i, float v, int i1) {
            mCurrentIndex = i;
            mCurrentPositionOffset = v;
            //HorizontalScrollView滚动
            scrollToChild(i, (int) (v * mLinearContainer.getChildAt(i).getWidth()));
            //invalidate后onDraw会被调用,绘制指示器
            invalidate();
        }

        @Override
        public void onPageSelected(int i) {
            mSelectedIndex = i;
            updateTextTabStyle();
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }

    /**
     * 滑动HorizontalScrollView
     * @param position          标示下标ID
     * @param currentTextLength 当前文本长度
     * 比较关键 ---- 执行scrollView的滑动效果
     *
     *  这是很清除的,当水平方向滑动,值为正向左滑动
     */
    private void scrollToChild(int position, int currentTextLength) {
        // 如果文本长度为空,或者平分模式则返回
        if (mTabCount == 0 || isExpand){
            return;
        }
        //getLeft():tab相对于父控件，即tabsContainer的left
        View child = mLinearContainer.getChildAt(position);
        int newScrollX = child.getLeft() + currentTextLength ;

        //附加一个偏移量，防止当前选中的tab太偏左
        //可以去掉看看是什么效果

        // 相当于在原来基础上向右滑 mScrollOffset的距离,就达到了太左或者太有可以往中间靠的效果
        if (position > 0 || currentTextLength > 0) {
            newScrollX -= mScrollOffset;
        }

        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }
    }

    /*----------------- 使用者可根据需要进行参数的修正 契合自己项目的需求-------------------*/

    /**
     * 设置指示器的高度
     * @param indicatorHeightDp dp为单位
     * @return
     */
    public NiceViewPagerIndicator setIndicatorHeight(int indicatorHeightDp){
        this.mIndicatorStrokeWidth = indicatorHeightDp;
        return this;
    }

    /**
     * 是否与文字等长 --------
     * @return
     */
    public NiceViewPagerIndicator setIndicatorLengthType(IndicatorType mIndicatorType){
        this.mIndicatorType = mIndicatorType;
        return this;
    }

    /**
     * 设置文本指示器的水品左右水品padding
     */
    public NiceViewPagerIndicator setIndicatorHorlPadding(int tabHorizontalPadding){
        this.mTabHorizontalPadding = tabHorizontalPadding;
        return this;
    }



    /**
     * 设置是否平分布局
     * @param isExpand boolean
     * @return
     */
    public NiceViewPagerIndicator setIsExpand(boolean isExpand){
        this.isExpand = isExpand;
        return this;
    }

    /**
     * 设置未选中的颜色
     * @param colorID
     * @return
     */
    public NiceViewPagerIndicator setNormalTextColor(int colorID){
        this.mNormalTextColor = colorID;
        return this;
    }

    /**
     * 设置选中的颜色
     * @param colorID
     * @return
     */
    public NiceViewPagerIndicator setSelectedTextColor(int colorID){
        this.mSelectedTextColor = colorID;
        return this;
    }

    /**
     * 设置为选中的文字的颜色
     * @param textNormalSp
     * @return
     */
    public NiceViewPagerIndicator setNormalTextSize(int textNormalSp){
        this.mNormalTextColor = textNormalSp;
        return this;
    }

    /**
     * 设置已经选中的文字的颜色
     * @param textSelectedSp
     * @return
     */
    public NiceViewPagerIndicator setSelectedTextSize(int textSelectedSp){
        this.mSelectedTextSize = textSelectedSp;
        return this;
    }

    /**
     * 设置指示器的长度
     * @param indicatorLength
     * @return
     */
    public NiceViewPagerIndicator setIndicatorLength(int indicatorLength){
        this.mIndicatorLength = indicatorLength;
        return this;
    }

}
