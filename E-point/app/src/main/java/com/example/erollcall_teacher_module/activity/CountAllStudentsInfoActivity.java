package com.example.erollcall_teacher_module.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.epoint.Activity.R;
import com.snowalker.tools.JsonTools;

/**
 * 获取所有学生信息
 *
 * @author snowalker
 *
 */
public class CountAllStudentsInfoActivity extends Activity {

	public static final int SHOW_ALL_STUDENTS = 0;
	// 服务端接口URL
	private static final String SERVICEURL = "http://119.29.3.138:8080/eback";
	private static final String CONTENTURL_LIST_ALLSTUDENTS = SERVICEURL
			+ "/webapi/teachers/students/all";
	//本机测试URL
	private static final String LOCALHOST_URL =
			"http://192.168.43.185:8080/callingTheRoll-manageSys/webapi/teachers/students/all";
	//ListView获取
	ListView show;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.count_all_students_info);

		show = (ListView) findViewById(R.id.show_all_students_info);

		countArrivedStudents(show);
	}

	/**
	 * 获取数据
	 */
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case SHOW_ALL_STUDENTS: {
					String str = (String) msg.obj;
					System.out.println(str);
					getListMapData(str);
					break;
				}

				default:
					break;
			}

		}

	};

	/**
	 * 按键回调listPerson
	 *
	 * @param v
	 */
	public void countArrivedStudents(View v) {

		new Thread() {
			public void run() {
				try {
					URL url = new URL(CONTENTURL_LIST_ALLSTUDENTS);
//					URL url = new URL(LOCALHOST_URL);
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection();
					connection.setConnectTimeout(5000);
					connection.setDoInput(true);
					connection.setRequestMethod("GET");
					int code = connection.getResponseCode();// 返回状态吗
					if (code == 200) {
						InputStream io = connection.getInputStream();
						byte[] by = new byte[1024];
						int len = 0;
						ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
						while ((len = io.read(by)) != -1) {
							byteArrayOutputStream.write(by, 0, len);
						}
						byte[] data = byteArrayOutputStream.toByteArray();
						String str = new String(data, "UTF-8");
						Message msg = new Message();
						msg.what = SHOW_ALL_STUDENTS;
						msg.obj = str;
						handler.sendMessage(msg);
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	/*
	 * 获取解析之后的list数据
	 */
	protected void getListMapData(String str) {
		List<Map<String, String>> data = JsonTools
				.getAllStudentsInfoFromJson(str);
		setView(data);

//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	/**
	 * 使用适配器设置数据
	 *
	 * @param data
	 */
	protected void setView(List<Map<String, String>> data) {
		// 设置listview
		SimpleAdapter adapter = new SimpleAdapter(
				CountAllStudentsInfoActivity.this, data, R.layout.all_students_data,
				new String[] { "stu_id","stu_name", "stu_classNum",
						"stu_college" }, new int[] { R.id.show_id,
				R.id.show_name, R.id.show_classNum, R.id.show_college });
		show.setAdapter(adapter);
	}
}
