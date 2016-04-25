package com.snowalker.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonTools {
	/**
	 * 获取所有学生
	 *
	 * @param str
	 * @return
	 */
	public static List<Map<String, String>> getAllStudentsInfoFromJson(
			String str) {
		List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
		/**
		 * jsonarray解析成功
		 */
		try {
			JSONArray sing = new JSONArray(str);
			for (int i = 0; i < sing.length(); i++) {
				JSONObject data = sing.getJSONObject(i);
				String id = data.getString("id");
				String name = data.getString("name");
				String classNum = data.getString("classNum");
				String college = data.getString("college");
				Map<String, String> map = new HashMap<String, String>();
				map.put("stu_id", id);
				map.put("stu_name", name);
				map.put("stu_classNum", classNum);
				map.put("stu_college", college);
				datas.add(map);
				System.out.println(id + name);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return datas;
	}

	/**
	 * 获取未到学生
	 *
	 * @param str
	 * @return
	 */
	public static List<Map<String, String>> getNotArrivedStudentsInfoFromJson(
			String str) {
		List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
		/**
		 * jsonarray解析成功
		 */
		try {
			JSONArray sing = new JSONArray(str);
			for (int i = 0; i < sing.length(); i++) {
				JSONObject data = sing.getJSONObject(i);
				String name = data.getString("name");
				String classNum = data.getString("classNum");
				String college = data.getString("college");
				Map<String, String> map = new HashMap<String, String>();
				map.put("stu_name", name);
				map.put("stu_classNum", classNum);
				map.put("stu_college", college);
				datas.add(map);
				System.out.println(name);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return datas;
	}

	public static List<Map<String, String>> getArrivedStudentsNumberFromJson(
			String str) {
		List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
		/**
		 * jsonarray解析成功 只需要解析一个属性countNum
		 */
		try {
			JSONArray sing = new JSONArray(str);
			for (int i = 0; i < sing.length(); i++) {
				JSONObject data = sing.getJSONObject(i);
				String countNum = data.getString("countNum");

				Map<String, String> map = new HashMap<String, String>();
				map.put("countNum", countNum);

				datas.add(map);
				System.out.println("总数" + countNum);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return datas;
	}
}
