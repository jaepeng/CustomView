package com.example.customview;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.customview.customview.agreeplaintext.AgreementTextView;

public class MainActivity extends Activity{

    private static final String TAG ="MainActivity" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AgreementTextView agreementTextView = this.findViewById(R.id.agreementTextView);
        agreementTextView.setOnCheckAgreementListener(new AgreementTextView.OnCheckAgreementListener() {
            @Override
            public void checked() {
                Toast.makeText(MainActivity.this, "成功啦!", Toast.LENGTH_SHORT).show();
            }
        });

//        KeypadView keypadView=findViewById(R.id.key_pad);
//        keypadView.setOnNumberClickListener(new KeypadView.OnNumberClickListener() {
//            @Override
//            public void onNumberClick(int value) {
//                Toast.makeText(MainActivity.this, "this is "+value, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onDeleteClick() {
//                Toast.makeText(MainActivity.this, "这就是删除!!", Toast.LENGTH_SHORT).show();
//            }
//        });







//        FlowLayout flowLayout=findViewById(R.id.flow_layout);
//        List<String> data=new ArrayList<>();
//        data.add("键盘啊");
//        data.add("显示器1");
//        data.add("显示器2");
//        data.add("显示器3");
//        data.add("显示器4");
//        data.add("显示器5");
//        data.add("显示器6");
//        data.add("显示器7");
//        data.add("显示器8");
//        data.add("ipod");
//        data.add("iphone");
//        flowLayout.setTextList(data);
//        flowLayout.setOnItemClickListener(new FlowLayout.OnItemClickListener() {
//            @Override
//            public void onItemClickListener(View view, String text) {
//                Toast.makeText(MainActivity.this, "点击了:"+text, Toast.LENGTH_SHORT).show();
//            }
//        });



    }

}
