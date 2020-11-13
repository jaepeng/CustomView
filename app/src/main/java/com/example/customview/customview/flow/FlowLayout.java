package com.example.customview.customview.flow;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.customview.R;
import com.example.customview.utils.SizeUtils;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup {
    public static final int DEFAULT_LINE=-1;
    public static final int DEFAULT_HORIZONTAL_MARGIN= SizeUtils.dip2px(5f);
    public static final int DEFAULT_VERTICAL_MARGIN=SizeUtils.dip2px(5f);
    public static final int DEFAULT_BORDER_RADIUS=SizeUtils.dip2px(5f);

    public static final int DEFAULT_MAX_LENGTH=-1;
    public static final int DEFAULT_LINES=3;
    private int mMaxLines;
    private int mHorizontalMargin;
    private int mVerticalMargin;
    private int mTextColor;
    private int mBorderColor;
    private float mBorderRadius;
    private static String TAG="FlowLayout";
    private int mTextmaxLength;
    private List<String> mData=new ArrayList<>();
    private OnItemClickListener mOnItemClickListener=null;


    //todo :要去转成px单位,这里还是integer
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View firstchild = getChildAt(0);
        int currentLeft= mHorizontalMargin+getPaddingLeft();
        int currentTop=  mVerticalMargin+getPaddingTop();
        int currentRight=  mHorizontalMargin+getPaddingLeft();
        int currentBottom=firstchild.getMeasuredHeight()+mVerticalMargin+getPaddingTop();
        for (List<View> line : mLines) {
            for (View view : line) {
                //布局每一行
                int width = view.getMeasuredWidth();
                currentRight+=width;
                //判断右边边界条件
                if (currentRight>getMeasuredWidth()/*这里是当前FlowLayout界面的值*/-mHorizontalMargin){
                    currentRight=getMeasuredWidth()-mHorizontalMargin;
                }
                view.layout(currentLeft,currentTop,currentRight,currentBottom);
                currentLeft=currentRight+ mHorizontalMargin;
                currentRight+= mHorizontalMargin;


            }
            currentTop+=getChildAt(0).getMeasuredHeight()+mVerticalMargin;
            currentBottom+=firstchild.getMeasuredHeight()+mVerticalMargin;
            currentLeft=  mHorizontalMargin+getPaddingLeft();
            currentRight=  mHorizontalMargin+getPaddingLeft();

        }

    }

    private List<List<View>> mLines=new ArrayList<>();

    /**
     * 这两个值来自父控件,包含值和模式
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int parentWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int parenHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        Log.d(TAG, "onMeasure: mode--->"+mode);
        Log.d(TAG, "onMeasure: size--->"+parentWidthSize);
        //测量孩子
        int childCount = getChildCount();
        if (childCount==0){
            return;
        }

        //先清空
        mLines.clear();
        List<View> line=new ArrayList<>();
        //添加默认行
        mLines.add(line);
        int childWidthSpace = MeasureSpec.makeMeasureSpec(parentWidthSize, MeasureSpec.AT_MOST);
        int childHeightSpace = MeasureSpec.makeMeasureSpec(parenHeightSize, MeasureSpec.AT_MOST);
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility()!=VISIBLE){
                continue;
            }
            //测量孩子
            measureChild(child,childWidthSpace,childHeightSpace);
            //判断是否可以添加
            if (line.size()==0){
                //可以添加
                line.add(child);
            }else{
                //判断是否可以调剂到当前行
                boolean canBeAdd= checkChildCanBeAdd(line,parentWidthSize,child);
                if (!canBeAdd){
                    if(mLines.size()>=mMaxLines){
                        //跳出循环
                        break;
                    }
                    line=new ArrayList<>();
                    mLines.add(line);
                }
                line.add(child);
            }
        }
        //拿到孩子尺寸,根据尺寸去算行高
        View childView=getChildAt(0);
        int childHeight = childView.getMeasuredHeight();
        int parentHeightTargetSize= childHeight*(mLines.size()+1)
                +mLines.size()* mVerticalMargin
                +getPaddingTop()
                +getPaddingBottom();



        //测量自己
        setMeasuredDimension(parentWidthSize,parentHeightTargetSize);
    }

    private boolean checkChildCanBeAdd(List<View> line, int parentWidthSize, View child) {
        int measuredWidth = child.getMeasuredWidth();
        int totalWidh=  mHorizontalMargin+getPaddingLeft();
        for (View view : line) {
            totalWidh+= view.getMeasuredWidth()+mHorizontalMargin;
        }
        totalWidh+=measuredWidth+mHorizontalMargin+getPaddingRight();

        //如果超出限制宽度,则不可以添加
        return totalWidh<=parentWidthSize;
    }

    public void setTextList(List<String> data){
        this.mData.clear();
        this.mData.addAll(data);
        //更具数据创建子View,并且添加进来
        setUpChildren();
    }

    private void setUpChildren() {
        //先清空原有内容
        removeAllViews();
        //添加子view
        for (final String datum : mData) {
            final TextView textView= (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_flow_text,this,false);
            if (mTextmaxLength!=DEFAULT_MAX_LENGTH){
                //设置最大长度
                textView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mMaxLines)});

            }
            textView.setText(datum);
            //todo:设置TextView相关属性:边距,颜色,边框...
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener!=null){
                        mOnItemClickListener.onItemClickListener(v,datum);
                    }
                }
            });
            addView(textView);//因为这里已经有添加动作了,如果上面最后加载为true的话,就会报错


        }
    }
    public void  setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener=onItemClickListener;
    }
    public interface OnItemClickListener{
        void onItemClickListener(View view,String text);
    }

    public FlowLayout(Context context) {
        this(context,null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        //maxLine
        mMaxLines = a.getInteger(R.styleable.FlowLayout_maxLine, DEFAULT_LINE);
        if (mMaxLines!=-1&&mMaxLines<1){
            throw new IllegalArgumentException("mMaxLines cannot <1");
        }
        //itemHorizontalMargin
        mHorizontalMargin =(int) a.getDimension(R.styleable.FlowLayout_itemHorizontalMargin, DEFAULT_HORIZONTAL_MARGIN);
        //itemVerticalMagin
        mVerticalMargin =  (int)a.getDimension(R.styleable.FlowLayout_itemHorizontalMargin, DEFAULT_VERTICAL_MARGIN);
        //textMaxLength
        mTextmaxLength = a.getInteger(R.styleable.FlowLayout_textMaxLength, DEFAULT_MAX_LENGTH);
        if (mTextmaxLength!=1&&mTextmaxLength<1){
            throw new IllegalArgumentException("TextMaxLength cannot <1");
        }
        //textColor
        mTextColor = a.getColor(R.styleable.FlowLayout_textColor, getResources().getColor(R.color.text_grey, null));
        //borderColor
        mBorderColor = a.getColor(R.styleable.FlowLayout_borderColor, getResources().getColor(R.color.text_grey, null));
        //borderRadio
        mBorderRadius = a.getDimension(R.styleable.FlowLayout_borderRadius, DEFAULT_BORDER_RADIUS);
        Log.d(TAG, "mMaxLines:--->"+mMaxLines);
        Log.d(TAG, "mHorizontalMargin:--->"+mHorizontalMargin);
        Log.d(TAG, "mVerticalMargin:--->"+mVerticalMargin);
        Log.d(TAG, "mTextmaxLength:--->"+mTextmaxLength);
        Log.d(TAG, "mTextColor:--->"+mTextColor);
        Log.d(TAG, "mBorderColor:--->"+mBorderColor);
        Log.d(TAG, "mBorderRadius:--->"+mBorderRadius);

        a.recycle();
    }

    public int getMaxLines() {
        return mMaxLines;
    }

    public void setMaxLines(int maxLines) {
        mMaxLines = maxLines;
    }

    public float getHorizontalMargin() {
        return mHorizontalMargin;
    }

    public void setHorizontalMargin(int horizontalMargin) {
        mHorizontalMargin = SizeUtils.dip2px(horizontalMargin);
    }

    public float getVerticalMargin() {
        return mVerticalMargin;
    }

    public void setVerticalMargin(int verticalMargin) {
        mVerticalMargin = SizeUtils.dip2px(verticalMargin);
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public void setBorderColor(int borderColor) {
        mBorderColor = borderColor;
    }

    public float getBorderRadius() {
        return mBorderRadius;
    }

    public void setBorderRadius(float borderRadius) {
        mBorderRadius = borderRadius;
    }

    public int getTextmaxLength() {
        return mTextmaxLength;
    }

    public void setTextmaxLength(int textmaxLength) {
        mTextmaxLength = textmaxLength;
    }
}
