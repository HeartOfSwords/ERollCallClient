package com.epoint.tools;

import android.util.Log;

import com.epoint.properites.ActivityJumpFlag;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by duanjigui on 2016/4/13.
 */

/**
 * 该类用于获取服务端的返回的信息，消息
 */
public class NetTools {
    /**
     * 定义请求头
     */
    public  final static String GET="GET";
    public  final static String HEAD="HEAD";
    public  final static String POST="POST";
    public  final static String PUT="PUT";
    public  final static String DELETE="DELETE";
    public  final static String OPTIONS="OPTIONS";
    public  static String GetStringFromService(String url,String method)throws IOException{
        String value=null;
        try{
            URL address=new URL(url);
            HttpURLConnection connection=(HttpURLConnection) address.openConnection();
            connection.setRequestMethod(method);
            connection.setConnectTimeout(5000);
            connection.setDoInput(true);
            int code=connection.getResponseCode();
            if (code==200){
                InputStream inputStream= connection.getInputStream();
                byte [] bytes=new byte[1024];
                int length=0;
                ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                while ((length=inputStream.read(bytes))>0){
                    byteArrayOutputStream.write(bytes,0,length);
                }
                value=new String(byteArrayOutputStream.toByteArray(),"utf-8");
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
            Log.d("url_error", "网址错误！");
        }
        return value;
    }

    /**
     *
     * @param id
     * @return boolean 返回是否已经签到
     * @throws IOException
     */
    public static boolean GetPersonInformation(String id) throws IOException {
        String s= GetStringFromService(ActivityJumpFlag.URL.QUERY_STUDENT_ALL_INFOR,GET);
        JsonPaser jsonPaser=new JsonPaser();
        if (jsonPaser.paser_person_info(s,id)==1){
            return  true;
        }else{
            return  false;
        }
    }

}
