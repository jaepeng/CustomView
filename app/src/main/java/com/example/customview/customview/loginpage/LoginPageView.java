package com.example.customview.customview.loginpage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.customview.R;
import com.example.customview.customview.App;

import java.lang.reflect.Field;

public class LoginPageView extends FrameLayout {
    public static final int SIZE_VERIFY_CODE_DEFAULT=4;
    private int mVerifyCodeSize;
    private int mMColor;
    private CheckBox mIsConfirm;
    private EditText mVerifyCodeEdt;
    private OnLoginPageActionListener mOnLoginPageActionListener =null;
    private LoginKeyBorad mInputNumber_pad;
    private EditText mPhoneNumEdt;
    private static final String TAG="LoginPageView";
    private TextView mGetVerifyCodeTv;
    private boolean isPhoneNumberOk=false;
    private boolean isAgreementOk=false;
    private boolean isVerifyCodeOk=false;
    //手机号码规则
    public static final String REGEX_MOBILE_EXACT = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$";
    public static final int DURATION_DEFAULT=60*1000;
    private View mLoginBtn;

    /**
     * 倒计时
     * 时长:duration
     * 时间间隔:1s(step)
     * 通知UI更新
     *
     */

    private Handler mHandler;
    private int mTotalDuration=DURATION_DEFAULT;
    private int mDTime=1000;
    private int mRestTime;
    private CountDownTimer mCountDownTimer;
    private TextView mAgreementTips;

    public void  startCountDown(){
        mTotalDuration = 60*1000;
        //间隔
        mDTime = 1000;
        mRestTime = mTotalDuration;
        mHandler = App.getmHandler();

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mRestTime -= mDTime;
                Log.d(TAG, "run: run: rest:-->"+ mRestTime);
                if (mRestTime >0) {
                    mHandler.postDelayed(this, mDTime);
                    mGetVerifyCodeTv.setText("("+ mRestTime /1000+"秒)");
                    mGetVerifyCodeTv.setEnabled(false);
                    isCountDowning=true;
                }else{
                    mGetVerifyCodeTv.setText("获取验证码");
                    mGetVerifyCodeTv.setEnabled(true);
                    isCountDowning=false;
                    updateAllBtnState();
                }
                //更新UI
            }
        });
    }

    private void begincountDown() {
        isCountDowning=true;
        mCountDownTimer = new CountDownTimer(mTotalDuration, 1000) {

            public void onTick(long millisUntilFinished) {
             //通知内容更新
                int res=(int)(millisUntilFinished/1000);
                mGetVerifyCodeTv.setText("("+ res+"秒)");
                mGetVerifyCodeTv.setEnabled(false);

            }

            public void onFinish() {
               //倒计时结束
                mGetVerifyCodeTv.setText("获取验证码");
                mGetVerifyCodeTv.setEnabled(true);
                isCountDowning=false;
                updateAllBtnState();
            }
        }.start();
    }
    //处理状态冲突
    private boolean isCountDowning=false;

    //验证码错误
    public void onVerifyCoedError(){

        //清空内容
        mVerifyCodeEdt.getText().clear();
        //在倒计时,将倒计时停止
        if (isCountDowning&&mCountDownTimer != null){
            isCountDowning=false;
            mCountDownTimer.cancel();
            mCountDownTimer.onFinish();
        }

    }
    public void onVerfifyCodeSent(){

    }
    //登录成功
    public void onSuccess(){

    }

    public LoginPageView(@NonNull Context context) {
        this(context,null);
    }

    public LoginPageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoginPageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //
        initAttrs(context, attrs);
        initView(context);
        disableEdtFocused2Keypad();
        initEvent();
    }
    private void updateAllBtnState(){
        if (!isCountDowning){
            mGetVerifyCodeTv.setEnabled(isPhoneNumberOk);
        }
        mLoginBtn.setEnabled(isPhoneNumberOk&&isAgreementOk&&isVerifyCodeOk);
        mAgreementTips.setTextColor(isAgreementOk? getResources().getColor(R.color.maincolor,null):getResources().getColor(R.color.maindeepcolor,null));



    }

    private void disableEdtFocused2Keypad() {
        mVerifyCodeEdt.setShowSoftInputOnFocus(false);
        mPhoneNumEdt.setShowSoftInputOnFocus(false);
    }

    private void initEvent() {
        mAgreementTips.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mInputNumber_pad.setOnKeyPressListener(new LoginKeyBorad.OnKeyPressListener() {
            @Override
            public void onNumberPress(int number) {
                //数字被点击
                //插入数字
                EditText foucusEdt = getFoucusEdt();
                if (foucusEdt != null) {
                    Editable text = foucusEdt.getText();
                    int selectionEnd = foucusEdt.getSelectionEnd();
                    text.insert(selectionEnd,String.valueOf(number));
                }
            }

            @Override
            public void onBackPress() {
                //返回键被点击
                EditText foucusEdt = getFoucusEdt();
                if (foucusEdt != null) {
                    int index = foucusEdt.getSelectionEnd();
                    Editable text = foucusEdt.getText();
                    if (index>0){

                        text.delete(index-1,index);
                    }


                }
            }
        });
        mGetVerifyCodeTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnLoginPageActionListener != null) {
                    //拿到手机号
                    String phoneNum = mPhoneNumEdt.getText().toString();
                    Log.d(TAG, "onClick: phoen num"+phoneNum);
                    mOnLoginPageActionListener.onGetVerifyCode(phoneNum);
                    //开启倒计
//                    startCountDown();
                    begincountDown();

                }else{
                    throw new IllegalArgumentException("no Action to verify code");
                }
            }
        });
        mPhoneNumEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //变化了,去检查手机号码是否符合格式
                Log.d(TAG, "onTextChanged:CharSequence-------> "+s);
                String phonenumber = s.toString();
                boolean isMatch = phonenumber.matches(REGEX_MOBILE_EXACT);
                isPhoneNumberOk=phonenumber.length()==11&&isMatch;
                updateAllBtnState();


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mLoginBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo 根据要求是否点击登录以后,是否禁止按钮
                //UI上防止重复提交
                if (mOnLoginPageActionListener != null) {
                    //手机号码验证码

                    mOnLoginPageActionListener.onConfimr(getVerifyCode(),getPhoneNumber());
                }
            }
        });
        mVerifyCodeEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isVerifyCodeOk=s.length()==4;
                updateAllBtnState();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mIsConfirm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isAgreementOk=isChecked;
                updateAllBtnState();
            }
        });
    }
    private String getPhoneNumber(){
        String phoneNumber = mPhoneNumEdt.getText().toString();
        if (phoneNumber!=null&&phoneNumber.length()==11){
            return phoneNumber;

        }
        return "0";
    }
    private String getVerifyCode(){
        String verifyCode = mVerifyCodeEdt.getText().toString();
        if (verifyCode.length()==4&& verifyCode!=null){
            return verifyCode;
        }
        return "0";
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.login_page_view, this, false);
        addView(view);
        mIsConfirm = this.findViewById(R.id.report_check_box);
        mVerifyCodeEdt = this.findViewById(R.id.verify_edt);
        mInputNumber_pad = this.findViewById(R.id.number_key_pad);
        mPhoneNumEdt = this.findViewById(R.id.phone_num_input);
        mPhoneNumEdt.requestFocus();
        mGetVerifyCodeTv = this.findViewById(R.id.getVerifyTv);
        mLoginBtn = this.findViewById(R.id.login_btn);
        mAgreementTips = this.findViewById(R.id.agreement_text_tips);
        disableCopyAndPaste(mPhoneNumEdt);
        disableCopyAndPaste(mVerifyCodeEdt);

        if (mMColor!=-1){
            mIsConfirm.setTextColor(mMColor);

        }
        if (mVerifyCodeSize!=SIZE_VERIFY_CODE_DEFAULT){
          mVerifyCodeEdt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mVerifyCodeSize)});
        }
    }

    private void initAttrs(@NonNull Context context, @Nullable AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoginPageView);
        mMColor = a.getColor(R.styleable.LoginPageView_mainColor, -1);
        mVerifyCodeSize = a.getInt(R.styleable.LoginPageView_verifyCodeSize, SIZE_VERIFY_CODE_DEFAULT);
        mTotalDuration= a.getInt(R.styleable.LoginPageView_countDownDuration, DURATION_DEFAULT);
        a.recycle();
    }
    public void setOnLoginPageActionListener(OnLoginPageActionListener listener){
        this.mOnLoginPageActionListener =listener;
    }

    /*
     *获取当前所有页面焦点
     * @return
     */
   private EditText getFoucusEdt(){
       View view = this.findFocus();
       if (view instanceof EditText){
           return (EditText) view;

       }
       return null;
   }

    public interface OnLoginPageActionListener{
        void  onGetVerifyCode(String phoneNumber);
        void onOpenAgreement();
        void onConfimr(String verifyCode,String phoneNumber);
    }
    public int getVerifyCodeSize() {
        return mVerifyCodeSize;
    }

    public void setVerifyCodeSize(int verifyCodeSize) {
        mVerifyCodeSize = verifyCodeSize;
    }

    public int getMColor() {
        return mMColor;
    }

    public void setMColor(int MColor) {
        mMColor = MColor;
    }
    @SuppressLint("ClickableViewAccessibility")
    public void disableCopyAndPaste(final EditText editText) {
        try {
            if (editText == null) {
                return ;
            }

            editText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            });
            editText.setLongClickable(false);
            editText.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        // setInsertionDisabled when user touches the view
                        setInsertionDisabled(editText);
                    }

                    return false;
                }
            });
            editText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getTotalDuration() {
        return mTotalDuration;
    }

    public void setTotalDuration(int totalDuration) {
        mTotalDuration = totalDuration;
    }

    private void setInsertionDisabled(EditText editText) {

        try {
            Field editorField = TextView.class.getDeclaredField("mEditor");
            editorField.setAccessible(true);
            Object editorObject = editorField.get(editText);

            // if this view supports insertion handles
            Class editorClass = Class.forName("android.widget.Editor");
            Field mInsertionControllerEnabledField = editorClass.getDeclaredField("mInsertionControllerEnabled");
            mInsertionControllerEnabledField.setAccessible(true);
            mInsertionControllerEnabledField.set(editorObject, false);

            // if this view supports selection handles
            Field mSelectionControllerEnabledField = editorClass.getDeclaredField("mSelectionControllerEnabled");
            mSelectionControllerEnabledField.setAccessible(true);
            mSelectionControllerEnabledField.set(editorObject, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
