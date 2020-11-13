package com.example.customview.customview.keypad;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.customview.R;
import com.example.customview.utils.SizeUtils;

public class KeypadView extends ViewGroup {

    private static final String TAG = "KeypadView";
    private static final int DEFAULT_ROW = 4;
    private static final int DEFAULT_COLUMN = 3;
    private static final int DEFAULT_MARGIN= SizeUtils.dip2px(2);

    private int mColor;
    private float mTextSize;
    private int mItembgRes;
    private int mItemPressColor;
    private int mItemNormalColor;
    private int column=DEFAULT_COLUMN;
    private int row=DEFAULT_ROW;
    private int mItemMargin;
    private OnNumberClickListener mOnNumberClickListener=null;


    public KeypadView(Context context) {
        this(context,null);
    }

    public KeypadView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public KeypadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        InitAttrs(context, attrs);
        setUpItem();
    }

    private void setUpItem() {
        removeAllViews();
        for (int i = 0; i < 11; i++) {
            final TextView item=new TextView(getContext());
            if (i==10){
                item.setTag(true);
                item.setText("删除");
            }else{
                item.setTag(false);
                item.setText(String.valueOf(i));
            }
            //大小尺寸,居中
            if (mTextSize!=-1) {

                item.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            }

            item.setGravity(Gravity.CENTER);
            //字体颜色
            item.setTextColor(getResources().getColor(R.color.white,getContext().getTheme()));
            //设置背景
            item.setBackground(providerItemBg());
            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //todo:
                    if (mOnNumberClickListener!=null){
                        if (v instanceof TextView){
                            String s = ((TextView) v).getText().toString();
//
                            boolean isDelete = (boolean) v.getTag();
                            if (isDelete){
                                mOnNumberClickListener.onDeleteClick();
                            }else{

                                mOnNumberClickListener.onNumberClick(Integer.parseInt(s));
                            }
                        }
                    }
                }
            });
            addView(item);

        }
    }
    private Drawable providerItemBg(){
        //按下去
        GradientDrawable pressDrawable=new GradientDrawable();
        pressDrawable.setColor(mItemPressColor);
        pressDrawable.setCornerRadius(SizeUtils.dip2px(5));
        StateListDrawable bg=new StateListDrawable();
        bg.addState(new int[]{android.R.attr.state_pressed},pressDrawable);
        //普通状态
        GradientDrawable normalDrawable=new GradientDrawable();
        normalDrawable.setColor(mItemNormalColor);
        normalDrawable.setCornerRadius(SizeUtils.dip2px(5));

        bg.addState(new int[]{},normalDrawable);
        return bg;
    }

    private void InitAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.KeypadView);
        //获取属性
        mColor = a.getColor(R.styleable.KeypadView_numberColor, context.getResources().getColor(R.color.key_item_color,null));
        mTextSize = a.getDimensionPixelSize(R.styleable.KeypadView_numberSize, -1);
        Log.d(TAG, "InitAttrs: textSize===>"+mTextSize);

        mItemPressColor = a.getColor(R.styleable.KeypadView_itemPressColor, context.getResources().getColor(R.color.key_item_press_color, null));
        mItemNormalColor = a.getColor(R.styleable.KeypadView_itemNormalColor, context.getResources().getColor(R.color.key_item_color, null));
        mItemMargin = (int) a.getDimension(R.styleable.KeypadView_itemMargin, DEFAULT_MARGIN);
        a.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int verticalPadding = getPaddingBottom() + getPaddingTop();
        int horizontalPadding = getPaddingRight() + getPaddingLeft();
        //测量孩子
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        Log.d(TAG, "widthSize: ===>"+widthSize);
        Log.d(TAG, "widthMode: ===>"+widthMode);
        //一行3列,求出列宽,三等分
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int perItemWidth=(widthSize-(column+1)*mItemMargin-horizontalPadding)/DEFAULT_COLUMN;
        int perItemHeight=(heightSize-(row+1)*mItemMargin-verticalPadding)/DEFAULT_ROW;
        int normalWidthSpec = MeasureSpec.makeMeasureSpec(perItemWidth, MeasureSpec.EXACTLY);
        int deleteWidthSpec=MeasureSpec.makeMeasureSpec(perItemWidth*2+mItemMargin, MeasureSpec.EXACTLY);
        int heightSpec = MeasureSpec.makeMeasureSpec(perItemHeight, MeasureSpec.EXACTLY);

        for (int i = 0; i < getChildCount(); i++) {
            View item = getChildAt(i);
            boolean isDelete = (boolean) item.getTag();
            item.measure(isDelete?deleteWidthSpec: normalWidthSpec,heightSpec);
        }

        //测量自己
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();

        int left=mItemMargin+paddingLeft,top,right,bottom;

        int childCount=getChildCount();
        for (int i = 0; i < childCount; i++) {
            //求出当前元素在第几行第几列
            int rowIndex=i/column;
            int columnIndex=i%column;
            if (columnIndex==0){
                left=mItemMargin+paddingLeft;
            }
            View item = getChildAt(i);
            top=rowIndex*item.getMeasuredHeight()+(rowIndex+1)*mItemMargin+paddingTop;
            right=left+item.getMeasuredWidth();
            bottom=top+item.getMeasuredHeight();
            item.layout(left,top,right,bottom);
            left+=item.getMeasuredWidth()+mItemMargin;


        }

    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float textSize) {
        mTextSize = textSize;
    }

    public int getItembgRes() {
        return mItembgRes;
    }

    public void setItembgRes(int itembgRes) {
        mItembgRes = itembgRes;
    }

    public int getItemPressColor() {
        return mItemPressColor;
    }

    public void setItemPressColor(int itemPressColor) {
        mItemPressColor = itemPressColor;
    }

    public int getItemNormalColor() {
        return mItemNormalColor;
    }

    public void setItemNormalColor(int itemNormalColor) {
        mItemNormalColor = itemNormalColor;
    }

    public int getItemMargin() {
        return mItemMargin;
    }

    public void setItemMargin(int itemMargin) {
        mItemMargin = itemMargin;
    }


    public void setOnNumberClickListener(OnNumberClickListener listener){
        this.mOnNumberClickListener=listener;
    }
    public interface OnNumberClickListener{
        void onNumberClick(int value);
        void onDeleteClick();
    }
}
