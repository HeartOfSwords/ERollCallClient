package com.epoint.tools;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by duanjigui on 2016/4/14.
 */
//封装对于传感器的一些操作
public abstract class Shakehelper implements SensorEventListener {
    private  Context context;
    private SensorManager sensorManager;
    private  Sensor sensor;
    private int mSpeed=3500;
    //时间间隔
    private int mInterval=50;
    //上一次摇晃的时间
    private long LastTime;
    //上一次的x、y、z坐标
    private float LastX,LastY,LastZ;
    public Shakehelper(Context context){
        this.context=context;
        this.start();
    }
    public  void  start(){
        sensorManager=(SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager!=null){
            sensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        if (sensor!=null){
            sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_GAME);
        }
    }
    public  void stop(){
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        long NowTime=System.currentTimeMillis();
        if((NowTime-LastTime)<mInterval)
            return;
        //将NowTime赋给LastTime
        LastTime=NowTime;
        //获取x,y,z
        float NowX=sensorEvent.values[0];
        float NowY=sensorEvent.values[1];
        float NowZ=sensorEvent.values[2];
        //计算x,y,z变化量
        float DeltaX=NowX-LastX;
        float DeltaY=NowY-LastY;
        float DeltaZ=NowZ-LastZ;
        //赋值
        LastX=NowX;
        LastY=NowY;
        LastZ=NowZ;
        //计算
        double NowSpeed = Math.sqrt(DeltaX * DeltaX + DeltaY * DeltaY + DeltaZ * DeltaZ)/mInterval * 10000;
        //判断
        if(NowSpeed>=mSpeed)
        {
            execute_opreation();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    public abstract void execute_opreation();
}
