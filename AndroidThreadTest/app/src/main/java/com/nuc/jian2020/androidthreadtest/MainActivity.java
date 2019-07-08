package com.nuc.jian2020.androidthreadtest;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textView = null;

    private Button changeTextBt = null;

    private static final int UPDATE_TEXT = 1;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_TEXT:
                    //这里进行UI操作
                    textView.setText("Nice to meet you");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.text);

        changeTextBt = (Button)findViewById(R.id.change_text);

        changeTextBt.setOnClickListener(new MyListener());
    }

    private class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.change_text:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message message = new Message();

                            message.what = UPDATE_TEXT;

                            handler.sendMessage(message);
                            //将Message对象发送出去
                        }
                    }).start();
                    break;
                    default:
                        break;
            }

        }
    }
}