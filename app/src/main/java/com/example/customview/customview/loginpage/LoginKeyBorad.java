package com.example.customview.customview.loginpage;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.customview.R;

public class LoginKeyBorad extends LinearLayout implements View.OnClickListener {
    public  static final String TAG="LoginKeyBorad";
    private OnKeyPressListener mOnKeyPressListener =null;

    public LoginKeyBorad(Context context) {
        this(context,null);
    }

    public LoginKeyBorad(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoginKeyBorad(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //
        View view = LayoutInflater.from(context).inflate(R.layout.num_key_pad, this, false);
        addView(view);
        initView();


    }

    private void initView() {
        this.findViewById(R.id.number_1).setOnClickListener(this);
        this.findViewById(R.id.number_2).setOnClickListener(this);
        this.findViewById(R.id.number_3).setOnClickListener(this);
        this.findViewById(R.id.number_4).setOnClickListener(this);
        this.findViewById(R.id.number_5).setOnClickListener(this);
        this.findViewById(R.id.number_6).setOnClickListener(this);
        this.findViewById(R.id.number_7).setOnClickListener(this);
        this.findViewById(R.id.number_8).setOnClickListener(this);
        this.findViewById(R.id.number_9).setOnClickListener(this);
        this.findViewById(R.id.number_0).setOnClickListener(this);
        this.findViewById(R.id.back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int vieId = v.getId();
        if (v instanceof TextView){

        }
        if (mOnKeyPressListener==null){
            Log.d(TAG, "mOnKeyPressListener is null ");
            return;
        }
        if (vieId==R.id.back){
            //back
            mOnKeyPressListener.onBackPress();

        }else{
            //数字
            String text = ((TextView) v).getText().toString();
            Log.d(TAG, "onClick: textContent->"+text);
            mOnKeyPressListener.onNumberPress(Integer.parseInt(text));

        }
    }
    public void setOnKeyPressListener(OnKeyPressListener listener){
        this.mOnKeyPressListener =listener;
    }

    public interface OnKeyPressListener{
        void onNumberPress(int number);
        void onBackPress();
    }
}
