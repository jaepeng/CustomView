package com.example.customview.utils;

import com.example.customview.customview.App;

public class SizeUtils {

    public static int dip2px( float dpValue) {
        float scale = App.getApplContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}