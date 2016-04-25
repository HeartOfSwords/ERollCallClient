package com.epoint.tools;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.epoint.properites.ActivityJumpFlag;

/**
 * Created by duanjigui on 2016/4/16.
 * 此类用于将状态持久化到文件中
 */
public abstract class StorgeFlag {
    /**
     * 用于将状态信息写入文件
     *
     * @param activity 那个活动
     * @param flag  当前用户是否点名
     * @param time  当前的时间
     */
    public void WriteInfo(Activity activity,boolean flag ,long time){
        SharedPreferences preferences= activity.getSharedPreferences("StatusInformation", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= preferences.edit();
        editor.putBoolean("Flag", flag);
        long times= preferences.getLong("Time", -1);
        if(times<time&&times!=-1&&time-times<10*60*1000){
            editor.putLong("Time",times);
        }else{
            editor.putLong("Time",time);
        }
        editor.putString("Student",ActivityJumpFlag.UserInfo.USER_ID);
        editor.commit();
    }

    /**
     *  读取文件里的状态信息
     *
     * @param activity
     */
    public  void ReadInfo(Activity activity){
        SharedPreferences preferences= activity.getSharedPreferences("StatusInformation", Context.MODE_PRIVATE);
        long time= preferences.getLong("Time", -1);
        boolean flag=preferences.getBoolean("Flag", false);
        String student= preferences.getString("Student", null);
        if ( ActivityJumpFlag.UserInfo.USER_ID.equals(student) ){
            ActivityJumpFlag.JumpFlag=flag;//重新覆盖标志位
            if (flag==false||(flag==true&& (System.currentTimeMillis()-time)>10*60*1000)){
                ActivityJumpFlag.JumpFlag=false;//大于10分钟重置为未点名状态
                this.unlock();//当大于30分后按钮解锁
            }else{
                lock();
            }
        }else{
            this.unlock();//当大于30分后按钮解锁
        }

    }
    //不锁定按钮事件
    public  abstract void unlock();
    //执行锁定按钮事件
    public  abstract void lock();
}
