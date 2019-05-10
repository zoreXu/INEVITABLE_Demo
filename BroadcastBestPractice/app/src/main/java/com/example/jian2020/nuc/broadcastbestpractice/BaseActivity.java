package com.example.jian2020.nuc.broadcastbestpractice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

//所以的活动的父类
public class BaseActivity extends AppCompatActivity {

    private ForceOfflineReceiver receiver = null;//广播接受器

    //活动的生命周期
    //创建BaseActivity类的子类在创建时就加入activities容器中
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);//静态方法直接通过类名调用
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.jian2020.nuc.broadcastbestpractice.FORCE_OFFLINE");
        receiver = new ForceOfflineReceiver();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (receiver != null){
            unregisterReceiver(receiver);
            receiver = null;//非栈顶的活动不许要接受广播
        }
    }

    //子类活动被摧毁后，从activities中删除掉
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    private class ForceOfflineReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(final Context context, Intent intent) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Warning");
            builder.setMessage("You are forced to be offline. Please try to login again.");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCollector.finishAll();//撤销所以的活动
                    Intent i = new Intent(context, LoginActivity.class);//返回到登入活动中
                    context.startActivity(i);//重新期待LoginActivity活动
                }
            });
            builder.show();
        }
    }
}
