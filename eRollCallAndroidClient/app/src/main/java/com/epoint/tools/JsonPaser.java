package com.epoint.tools;

import android.os.Handler;
import android.os.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by duanjigui on 2016/4/13.
 */

/**
 * 该类用于解析json格式的字符串
 */
public class JsonPaser {
    public  static final String ARRARY="json_array";
    public  static  final  String OBJECT="json_object";
    //返回类型为list，方便SimpleAdpter适配器的数据适配

    /**
     *
     * @param json  所需要的json格式的字符串
     * @param first_type 返回json数组的第一个类型
     * @return List<Map<String,Object>> 返回一个在simpleadpater中需要用到的类型
     * @throws Exception 输入json返回值的整体格式错误
     */
    public List<Map<String,Object>> PaserJson(String json,String first_type) throws Exception {
        List<Map<String,Object>> list=new ArrayList<Map<String, Object>>();
        if (JsonPaser.ARRARY.equals(first_type)){
            JSONArray array=new JSONArray(json);
            for (int i=0;i<array.length();i++){
                Map<String,Object> map=new HashMap<String, Object>();
                JSONObject object= array.getJSONObject(i);
                map.put("classNum",object.getString("classNum"));
                map.put("college",object.getString("college"));
                map.put("id",object.getString("id"));
                map.put("name",object.getString("name"));
                list.add(map);
            }
        }else if (JsonPaser.OBJECT.equals(first_type)){
            JSONObject object=new JSONObject(json);
            Map<String,Object> map=new HashMap<String, Object>();
            map.put("classNum",object.getString("classNum"));
            map.put("college",object.getString("college"));
            map.put("id",object.getString("id"));
            map.put("name",object.getString("name"));
            list.add(map);
        }else {
            throw  new Exception("输入json返回值的整体格式错误！");
        }
        return list;
    }
    public int paser_person_info(String json ,String id){
        try {
            JSONArray jsonArray=new JSONArray(json);
            for (int i=0;i<jsonArray.length();i++){
                  JSONObject jsonObject= jsonArray.getJSONObject(i);
                if (jsonObject.getString("id").equals(id)){
                    return jsonObject.getInt("counter");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -255;
    }
}
