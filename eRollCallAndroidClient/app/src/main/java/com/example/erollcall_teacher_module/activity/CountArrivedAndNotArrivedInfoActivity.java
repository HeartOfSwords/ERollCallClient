package com.example.erollcall_teacher_module.activity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.snowalker.erollcall.R;
import com.snowalker.tools.JsonTools;

/**
 * 获取到场学生人数
 *
 * @author snowalker
 *
 */
public class CountArrivedAndNotArrivedInfoActivity extends Activity {

	private static final int SHOW_NOT_ARRIVED_STUDENTS = 0;

	// 服务端接口URL
	private static final String SERVICEURL = "http://119.29.3.138:8080/eback";

	// 获取到达学生总数
	private static final String ARRIVED_STUDENS_NUMBER = SERVICEURL
			+ "/webapi/teachers/total/arrived/students";

	//private static final String ARRIVED_STUDENS_NUMBER = "http://192.168.7.109:8080/callingTheRoll-manageSys/webapi/teachers/total/arrived/students";
	// 获取未到达学生信息
	private static final String NOT_ARRIVED_STUDENTS_INFO = SERVICEURL
			+ "/webapi/teachers/info/notarrived";

	private TextView numOfArrived;// 显示学生到达数量
	private ListView showNotArrivedStudentsInfo;// 显示未到学生人员信息

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.count_arrivednum_not_arrivedinfo);

		numOfArrived = (TextView) findViewById(R.id.arrived_students_number);

		showNotArrivedStudentsInfo = (ListView) findViewById(R.id.Lshow_not_arrived_students_info);
		//保存学生信息
		saveGotStudentsInfo = (Button) findViewById(R.id.save_arrived_info_button);
		// 显示未到达学生列表
		showNotArrivedStudentsList(showNotArrivedStudentsInfo);
		// 显示到达学生总数
		countArrivedStudentNumber(numOfArrived);

		//保存学生信息
		saveGotStudentsInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				saveStudentsArrivedInfo();
				Toast.makeText(CountArrivedAndNotArrivedInfoActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void saveStudentsArrivedInfo() {
		new Thread() {
			public void run() {
				try {
					URL url = new URL(NOT_ARRIVED_STUDENTS_INFO);
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
						System.out.println(str);
						//保存数据
						TrulySavedFile(str);
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			private void TrulySavedFile(String str) {
				FileOutputStream out = null;
				BufferedWriter writer = null;
				try {
					out = openFileOutput("notArrived", Context.MODE_PRIVATE);
					writer = new BufferedWriter(new OutputStreamWriter(out));
					writer.write(str);

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (writer != null) {
							writer.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	/**
	 * 获取数据
	 */
	Handler handler1 = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case SHOW_NOT_ARRIVED_STUDENTS: {
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
	 * 获取学生人数string
	 */
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			String str = (String) msg.obj;
			System.out.println(str);
			numOfArrived.setText(str);
		}

	};

	private Button saveGotStudentsInfo;

	/**
	 * 获取未到达学生名单
	 *
	 * @param showNotArrivedStudentsInfo2
	 */
	private void showNotArrivedStudentsList(View v) {
		new Thread() {
			public void run() {
				try {
					URL url = new URL(NOT_ARRIVED_STUDENTS_INFO);
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
						msg.what = SHOW_NOT_ARRIVED_STUDENTS;
						msg.obj = str;
						handler1.sendMessage(msg);
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
				.getNotArrivedStudentsInfoFromJson(str);
		setView(data);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 使用适配器设置数据
	 *
	 * @param data
	 */
	protected void setView(List<Map<String, String>> data) {
		// 设置listview
		SimpleAdapter adapter = new SimpleAdapter(
				CountArrivedAndNotArrivedInfoActivity.this, data,
				R.layout.not_arrived_students_data, new String[] { "stu_id",
				"stu_name", "stu_classNum", "stu_college" }, new int[] {
				R.id.show_id, R.id.show_name, R.id.show_classNum,
				R.id.show_college });
		showNotArrivedStudentsInfo.setAdapter(adapter);
	}

	/**
	 * 计算到达学生人数 使用HttpClient请求
	 *
	 * @param v
	 */
	private void countArrivedStudentNumber(View v) {
		new Thread() {
			public void run() {

				try {

					URL url = new URL(ARRIVED_STUDENS_NUMBER);
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection();
					connection.setConnectTimeout(3000);// 有效时间
					connection.setRequestMethod("GET");// get方式
					int code = connection.getResponseCode();
					if (code == 200) {
						InputStream io = connection.getInputStream();
						BufferedInputStream bis = new BufferedInputStream(io);
						InputStreamReader reader = new InputStreamReader(bis);
						BufferedReader br = new BufferedReader(reader);
						String countNum = br.readLine();
						System.out.println("到场人数" + countNum);
						Message msg = new Message();
						msg.obj = countNum;
						handler.sendMessage(msg);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}.start();

	}

}
