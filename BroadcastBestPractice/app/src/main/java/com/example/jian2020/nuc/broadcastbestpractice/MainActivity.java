package com.example.jian2020.nuc.broadcastbestpractice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends BaseActivity {

    private Button forceofflineBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        forceofflineBtn = (Button) findViewById(R.id.force_offlineBtn);

        forceofflineBtn.setOnClickListener(new MyLinstener());
    }

    private class MyLinstener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent("com.example.jian2020.nuc.broadcastbestpractice.FORCE_OFFLINE");
            sendBroadcast(intent);
        }
    }
}
