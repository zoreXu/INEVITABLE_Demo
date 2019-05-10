package com.example.jian2020.nuc.broadcastbestpractice;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<>();

    //增加活动
    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    //移除活动
    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    //清楚所以的活动
    public static void finishAll(){
        for(Activity activity : activities){
            activity.finish();//遍历所以的活动，并关闭他们
        }
        activities.clear();
    }
}
