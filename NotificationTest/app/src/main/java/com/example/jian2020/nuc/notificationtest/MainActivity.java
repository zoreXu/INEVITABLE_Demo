package com.example.jian2020.nuc.notificationtest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button sendNoticebt = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        sendNoticebt.setOnClickListener(this);
    }

    private void init(){

        sendNoticebt = (Button) findViewById(R.id.send_notice);
    }


    @Override
    public void onClick(View v) {

        NotificationManager manager = null;

        NotificationChannel channel = null;

        Notification notification = null;

        switch (v.getId()){
            case R.id.send_notice:

                Intent intent = new Intent(MainActivity.this, NotificationActivity.class); //想启动NotificationActivity的意图
                PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);

                String channelId = "notification_send";
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {//安卓o以上
                    manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    //调用getSystemService 确定系统的通知服务，可以选择其他服务类型

                    channel = new NotificationChannel(channelId, "simple", NotificationManager.IMPORTANCE_DEFAULT);
                    manager.createNotificationChannel(channel);

                    notification = new NotificationCompat.Builder(MainActivity.this, channelId)
                            .setContentTitle("This is content title")  //设置通知标题
                            .setContentText("This is content text") //设置通知的内容
                            .setWhen(System.currentTimeMillis()) //通知被创建时间
                            .setSmallIcon(R.mipmap.ic_launcher) //设置通知的小标题
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                                    R.mipmap.ic_launcher)) //设置通知的大标题
                            .setContentIntent(pi)
                            .setAutoCancel(true)//消息实现点击后自动消失
                            .setVibrate(new long[]{0, 1000, 1000, 1000})//振动一秒，静止一秒，在振动一秒
                            .setLights(Color.GREEN, 1000, 1000) //设置呼吸灯
                            .setDefaults(NotificationCompat.DEFAULT_ALL)
                            .setStyle(new NotificationCompat.BigTextStyle().bigText("learn how to build notifications, send and sync data, and use actions." +
                                    "Get the offical Android IDE and developer tools to build for Android"))//设置长文字
                            .setPriority(NotificationCompat.PRIORITY_MAX) //设置通知的重要程度
                            .build();

                }
                else{
                    manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    notification = new NotificationCompat.Builder(MainActivity.this, channelId)
                            .setContentTitle("This is content title")
                            .setContentText("This is content text")
                            .setWhen(System.currentTimeMillis())
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                            .setContentIntent(pi)
                            .setAutoCancel(true)
                            .setVibrate(new long[]{0, 1000, 1000, 1000})//振动一秒，静止一秒，在振动一秒
                            .setLights(Color.GREEN, 1000, 1000)
                            .setDefaults(NotificationCompat.DEFAULT_ALL)
                            .build();
                    Toast.makeText(MainActivity.this,"lest 26",Toast.LENGTH_LONG).show();
                }

                manager.notify(1, notification);
                break;
            default:
                break;
        }
    }

}
