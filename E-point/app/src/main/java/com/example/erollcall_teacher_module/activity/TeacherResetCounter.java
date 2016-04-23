package com.example.erollcall_teacher_module.activity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.epoint.Activity.R;

/**
 * 教师重置计数器
 * @author snowalker
 *
 */
public class TeacherResetCounter extends Activity {

	private Button resetCounterBut;

	private String RESET_COUNTER_TO_ZERO = "http://119.29.3.138:8080/eback/webapi/teachers/reset/studentcounter";

	//	private String RESET_COUNTER_TO_ZERO = "http://192.168.7.124:8080/callingTheRoll-manageSys/webapi/teachers/reset/studentcounter";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.teacher_reset_couter);

		resetCounterBut = (Button) findViewById(R.id.resetBut);

		resetCounterBut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ResetCounterToZero();
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				jumpToMenu();
			}

		});
	}

	/**
	 * 获取回调字符串
	 */
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			String str = (String) msg.obj;
			System.out.println(str);
//			numOfArrived.setText(str);
			Toast.makeText(getApplicationContext(), str,
					Toast.LENGTH_SHORT).show();
		}

	};

	private void jumpToMenu() {
		Intent intent = new Intent(TeacherResetCounter.this,
				TeacherMenuActivity.class);
		startActivity(intent);
	}

	/**
	 * 重置计数器为0
	 */
	private void ResetCounterToZero() {
		new Thread() {
			public void run() {
				try {

					URL url = new URL(RESET_COUNTER_TO_ZERO);
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection();
					connection.setConnectTimeout(3000);// 有效时间
					connection.setRequestMethod("PUT");// PUT方式
					int code = connection.getResponseCode();
					if (code == 200) {
						InputStream io = connection.getInputStream();
						BufferedInputStream bis = new BufferedInputStream(io);
						InputStreamReader reader = new InputStreamReader(bis);
						BufferedReader br = new BufferedReader(reader);
						String callBackString = br.readLine();
						System.out.println("到场人数" + callBackString);
						Message msg = new Message();
						msg.obj = callBackString;
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

			};
		}.start();
	}

}
