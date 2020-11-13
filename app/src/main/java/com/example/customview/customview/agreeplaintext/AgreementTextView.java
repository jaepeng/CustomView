package com.example.customview.customview.agreeplaintext;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.CycleInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.customview.R;
import com.example.customview.utils.SizeUtils;

public class AgreementTextView extends LinearLayout {
    private static final int DEFAULT_AGRTEXT_SIZE = SizeUtils.dip2px(20);
    private static final String TAG="AgreementTextView";
    private static final float DEFAULT_CKBOX_SIZE = 1.0f;
    private static final String DEFAULT_CONTENT = "默认文字";
    private static final int DEFALULT_CONFIMR_TEXT_SIZE = SizeUtils.dip2px(15);
    private static final int DEFAULT_BTN_COLOR = R.color.white;
    private static final int DEFAULT_CONFIRM_BTN_HEIGHT = 100;
    private static final int DEFAULT_CONFIRM_BTN_WIDTH = 100;
    private static final int DEFAULT_BTN_CONTENT_COLOR =R.color.white;
    private int mAgrTextSize;
    private float mCkBoxSize;
    private int mAgrTextColor;
    private int mCkBoxBackground;
    private TextView mAgrContentTv;
    private CheckBox mAgrCkBox;
    private String mAgrContent1;
    private String mMAgrContent;
    private OnCheckAgreementListener onCheckedAgreementListener=null;
    private Animation mContentTranslateAnim;
    private float mConfirmBtnTextSize;
    private int mAgreeConfirmBackgroundColor;
    private int mConfirmBtnHeight;
    private int mConfirmBtnWidth;
    private Button mConfirmBtn;
    private String mConfirmBtnContent;
    private int mBtn_content_color;
    private LinearLayout mContainerVIew;

    public AgreementTextView(Context context) {
        this(context,null);
    }

    public AgreementTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AgreementTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initVIew(context);
        setUpItem();
        initEvent();

    }

    private void initEvent() {
        mConfirmBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAgrCkBox.isChecked()){
                    if (onCheckedAgreementListener!=null){
                        onCheckedAgreementListener.checked();
                    }
                }else{
                    contentAnimation();
                    Toast.makeText(getContext(), "请同意协议!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setUpItem() {
        //协议说明设置
        mAgrContentTv.setText(mMAgrContent);
        mAgrContentTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,mAgrTextSize);
        mAgrContentTv.setTextColor(mAgrTextColor);


        //CheckBox设置
        mAgrCkBox.setBackgroundResource(mCkBoxBackground);
        mAgrCkBox.setScaleX(mCkBoxSize);
        mAgrCkBox.setScaleY(mCkBoxSize);


        //确认按钮设置
        LayoutParams lp;
        lp= (LayoutParams) mConfirmBtn.getLayoutParams();
        lp.width=mConfirmBtnWidth;
        lp.height=mConfirmBtnHeight;
        mConfirmBtn.setLayoutParams(lp);
        mConfirmBtn.setText(mConfirmBtnContent);
        mConfirmBtn.setTextSize(TypedValue.COMPLEX_UNIT_DIP,mConfirmBtnTextSize);
        mConfirmBtn.setTextColor(mBtn_content_color);
        mConfirmBtn.setBackgroundResource(mAgreeConfirmBackgroundColor);


    }


    private void initVIew(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.agree_plaintext_view, this, false);
        addView(view);
        mAgrContentTv = this.findViewById(R.id.agree_agreementContent_tv);
        mAgrCkBox = this.findViewById(R.id.agree_agreement_cbox);
        mConfirmBtn = this.findViewById(R.id.agree_confirm_btn);
        mContainerVIew = this.findViewById(R.id.container_view);
    }

    private void initAttrs(Context context, @Nullable AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AgreementTextView);
        //CheckBox属性获取
        mCkBoxSize =a.getFloat(R.styleable.AgreementTextView_checkboxSize, DEFAULT_CKBOX_SIZE);
        mCkBoxBackground = a.getResourceId(R.styleable.AgreementTextView_checkboxBackground, -1);
        //协议TextView属性获取
        mAgrTextSize = (int)a.getDimension(R.styleable.AgreementTextView_agreementSize, DEFAULT_AGRTEXT_SIZE);
        mAgrTextColor = a.getColor(R.styleable.AgreementTextView_agreementColor, getResources().getColor(R.color.text_grey, getContext().getTheme()));
        mMAgrContent = a.getString(R.styleable.AgreementTextView_agreementContent);
        if (mMAgrContent==null||mMAgrContent.equals("")){
            mMAgrContent=DEFAULT_CONTENT;
        }
        //按钮属性获取
        mConfirmBtnTextSize = a.getDimension(R.styleable.AgreementTextView_agreeConfirmTextSize, DEFALULT_CONFIMR_TEXT_SIZE);
        mAgreeConfirmBackgroundColor = a.getResourceId(R.styleable.AgreementTextView_agreeConfirmBackground, -1);
        mConfirmBtnHeight = a.getInteger(R.styleable.AgreementTextView_agreeConfirmBtnHeight, DEFAULT_CONFIRM_BTN_HEIGHT);
        mConfirmBtnWidth = a.getInteger(R.styleable.AgreementTextView_agreeConfirmBtnWidth, DEFAULT_CONFIRM_BTN_WIDTH);
        mConfirmBtnContent = a.getString(R.styleable.AgreementTextView_agreeConfirmContent);
        mBtn_content_color = a.getColor(R.styleable.AgreementTextView_agreeConfirmTextColor, getResources().getColor(R.color.white, getContext().getTheme()));
        if (mConfirmBtnContent ==null|| mConfirmBtnContent.equals("")){
            mConfirmBtnContent =DEFAULT_CONTENT;
        }
        a.recycle();
        Log.d(TAG, "initAttrs: mConfirmBtnHeight--->"+mConfirmBtnHeight);
        Log.d(TAG, "initAttrs: mConfirmBtnWidth--->"+mConfirmBtnWidth);
        Log.d(TAG, "initAttrs: mConfirmBtnContent--->"+mConfirmBtnContent);
        Log.d(TAG, "initAttrs: mAgreeConfirmColor--->"+ mAgreeConfirmBackgroundColor);
    }


    public void contentAnimation(){
        mContentTranslateAnim = AnimationUtils.loadAnimation(getContext(), R.anim.agr_conten_translate_anim);
        mContentTranslateAnim.setInterpolator(new CycleInterpolator(3));
        mContainerVIew.startAnimation(mContentTranslateAnim);
    }

    public void setOnCheckAgreementListener(OnCheckAgreementListener listener){
        this.onCheckedAgreementListener=listener;
    }
    public interface OnCheckAgreementListener{
        void checked();
    }









    public int getAgrTextSize() {
        return mAgrTextSize;
    }

    public void setAgrTextSize(int agrTextSize) {
        mAgrTextSize = agrTextSize;
    }

    public float getCkBoxSize() {
        return mCkBoxSize;
    }

    public void setCkBoxSize(float ckBoxSize) {
        mCkBoxSize = ckBoxSize;
    }

    public int getAgrTextColor() {
        return mAgrTextColor;
    }

    public void setAgrTextColor(int agrTextColor) {
        mAgrTextColor = agrTextColor;
    }

    public int getCkBoxBackground() {
        return mCkBoxBackground;
    }

    public void setCkBoxBackground(int ckBoxBackground) {
        mCkBoxBackground = ckBoxBackground;
    }

    public String getMAgrContent() {
        return mMAgrContent;
    }

    public void setMAgrContent(String MAgrContent) {
        mMAgrContent = MAgrContent;
    }
}
