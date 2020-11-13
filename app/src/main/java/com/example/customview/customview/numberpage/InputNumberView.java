package com.example.customview.customview.numberpage;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.customview.R;

public class InputNumberView extends RelativeLayout {
    private static final String TAG = "InputNumberView";
    private int currentNumber=0;
    private View mPlusBtn;
    private EditText mValueEdt;
    private View mMinusBtn;
    private OnNumberChangeListener mOnNumberChangeListener=null;
    private int mMax;
    private int mMin;
    private int mStep;
    private int mDefaultValue;
    private boolean mDisable;
    private int mBtnBgRes;
    private Toast mToast;


    public InputNumberView(Context context) {
        this(context,null);
    }

    public InputNumberView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    @Override
    public String toString() {
        return "InputNumberView{" +
                "mMax=" + mMax +
                ", mMin=" + mMin +
                ", mStep=" + mStep +
                ", mDefaultValue=" + mDefaultValue +
                ", mDisable=" + mDisable +
                ", mBtnBgRes=" + mBtnBgRes +
                '}';
    }

    public InputNumberView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取相关属性
        initAttributes(context, attrs);
        initView(context);

        //处理事件
        setUpEvent();


    }

    private void initAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.InputNumberView);
        mMax = a.getInt(R.styleable.InputNumberView_max, 0);
        mMin = a.getInt(R.styleable.InputNumberView_min, 0);
        mStep = a.getInt(R.styleable.InputNumberView_step, 1);
        mDefaultValue = a.getInt(R.styleable.InputNumberView_defaultValue, 0);
        mDisable = a.getBoolean(R.styleable.InputNumberView_disable, false);
        mBtnBgRes = a.getResourceId(R.styleable.InputNumberView_btnBackground, -1);
        this.currentNumber=mDefaultValue;
        a.recycle();
    }

    private void setUpEvent() {
        mMinusBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlusBtn.setEnabled(true);
                currentNumber-=mStep;
                if (mMin!=0&&currentNumber<=mMin){
                    currentNumber=mMin;
                    mMinusBtn.setEnabled(false);
                    Toast.makeText(getContext(), "最小值!!!", Toast.LENGTH_SHORT).show();
                    if (mOnNumberChangeListener!=null) {

                        mOnNumberChangeListener.onValueInMin(currentNumber);
                    }
                }else{
                    mMinusBtn.setEnabled(true);
                }
                updateText();
            }
        });
        mPlusBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mMinusBtn.setEnabled(true);
                currentNumber+=mStep;
                if (mMax!=0&&currentNumber>=mMax){
                    currentNumber=mMax;
                    Toast.makeText(getContext(), "已经是最大值了!", Toast.LENGTH_SHORT).show();
                    mPlusBtn.setEnabled(false);
                    if (mOnNumberChangeListener!=null) {

                        mOnNumberChangeListener.onValueInMax(currentNumber);
                    }

                }else{
                    mPlusBtn.setEnabled(true);
                }
                updateText();

            }
        });
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.input_number_view,this,false);
        addView(view);
        mPlusBtn = this.findViewById(R.id.plus_btn);
        mValueEdt = this.findViewById(R.id.number_edt);
        mMinusBtn = this.findViewById(R.id.minus_btn);
        //初始化控件值
        updateText();
//        mMinusBtn.setEnabled(!mDisable);
//        mPlusBtn.setEnabled(!mDisable);

    }

    public int getNumber() {
        return currentNumber;
    }

    public void setNumber(int Number) {
        this.currentNumber = Number;
        updateText();

    }

    /**
     * 更新数据
     */
    public void updateText(){
        mValueEdt.setText(String.valueOf(currentNumber));
        if (mOnNumberChangeListener!=null){
            mOnNumberChangeListener.onValueChange(currentNumber);
        }

    }

    public void  setOnNumberChangeListener(OnNumberChangeListener listener){
        this.mOnNumberChangeListener=listener;
    }
    public interface OnNumberChangeListener{
        void onValueChange(int value);
        void onValueInMin(int value);
        void onValueInMax(int value);

    }

    public int getMax() {
        return mMax;
    }

    public void setMax(int max) {
        mMax = max;
    }

    public int getMin() {
        return mMin;
    }

    public void setMin(int min) {
        mMin = min;
    }

    public int getStep() {
        return mStep;
    }

    public void setStep(int step) {
        mStep = step;
    }

    public int getDefaultValue() {
        return mDefaultValue;
    }

    public void setDefaultValue(int defaultValue) {
        mDefaultValue = defaultValue;
        currentNumber=defaultValue;
        updateText();
    }

    public boolean isDisable() {
        return mDisable;
    }

    public void setDisable(boolean disable) {
        mDisable = disable;
    }

    public int getBtnBgRes() {
        return mBtnBgRes;
    }

    public void setBtnBgRes(int btnBgRes) {
        mBtnBgRes = btnBgRes;
    }
}
