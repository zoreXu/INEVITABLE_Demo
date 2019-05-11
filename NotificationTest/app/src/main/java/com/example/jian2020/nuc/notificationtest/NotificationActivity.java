package com.example.jian2020.nuc.notificationtest;

import android.app.NotificationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.cancel(1);
        //显示取消id为1的消息
    }
}
