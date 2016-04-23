package com.epoint.tools;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duanjigui on 2016/4/16.
 * 活动回收器
 */
public class ActiveContainer {
    private static List<Activity> list=new ArrayList<Activity>();

    /**
     *  返回回收器中的实现类
     * @return
     */
    public  static  List<Activity> GetActivityMangment(){
        return  list;
    }

    /**
     *  将活动添加到回收器中
     * @param activity
     */
    public static  void addActivity(Activity activity){
        list.add(activity);
    }

    /**
     * 退出
     */
    public  static  void close(){
        for (Activity context:list){
            context.finish();
        }
    }
    public  static void  removeActivity(Activity activity){
        list.remove(activity);
    }
}
