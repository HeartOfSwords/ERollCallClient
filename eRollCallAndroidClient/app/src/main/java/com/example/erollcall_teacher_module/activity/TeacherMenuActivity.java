package com.example.erollcall_teacher_module.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.snowalker.erollcall.R;
import com.epoint.properites.ActivityJumpFlag;
import com.epoint.tools.NetTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * 目录
 * @author snowalker
 *
 */
public class TeacherMenuActivity extends Activity {

	public static final int SHOW_ALL_STUDENTS = 0;

	private Button countArrivedNumAndNotArrivedInfo;
	private Button getStudentsInfo;
	private Button getLogedTeacherInfo;
	private Button jumpToAboutUs;

	// private ListView show;

	// 应用url,这台机器本地地址10.0.0.2
	private static final String SERVICEURL = "http://119.29.3.138:8080/eback";
	private static final String CONTENTURL_LIST_ALLSTUDENTS = SERVICEURL
			+ "/webapi/teachers/students/all";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.teacher_menu);

		// show = (ListView) findViewById(R.id.show);
		// 获取到场学生总数及未到学生信息
		countArrivedNumAndNotArrivedInfo = (Button) findViewById(R.id.count_arrived_students_num);
		// 获取所有学生信息
		getStudentsInfo = (Button) findViewById(R.id.get_students_info);
		// 获取登录的教师信息
		getLogedTeacherInfo = (Button) findViewById(R.id.get_loged_teacher_info);
		// 跳转到关于我们
		jumpToAboutUs = (Button) findViewById(R.id.about_us);

		// 未到信息及到达数量回调
		countArrivedNumAndNotArrivedInfo
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(TeacherMenuActivity.this,
								CountArrivedAndNotArrivedInfoActivity.class);
						startActivity(intent);
					}
				});
		// 获取所有信息回调
		getStudentsInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TeacherMenuActivity.this,
						CountAllStudentsInfoActivity.class);
				startActivity(intent);
			}
		});
		// 获取登录的教师信息
		getLogedTeacherInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new Thread(){
					@Override
					public void run() {
						Intent intent = new Intent(TeacherMenuActivity.this,
								GetLogedTeacherInfo.class);
						try {
							String info=	NetTools.GetStringFromService("http://119.29.3.138:8080/eback/webapi/teachers/"+ URLEncoder.encode(ActivityJumpFlag.UserInfo.USER_ID,"utf-8"), NetTools.GET);
							Log.d("info",info);
							JSONObject object=new JSONObject(info);
							String college=	object.getString("college");
							String name=	object.getString("name");
							intent.putExtra("college",college);
							intent.putExtra("name",name);
						} catch (IOException e) {
							e.printStackTrace();
						} catch (JSONException e) {
							e.printStackTrace();
						}


						startActivity(intent);
					}
				}.start();

			}
		});
		//关于我们回调
		jumpToAboutUs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TeacherMenuActivity.this,
						AboutUsActivity.class);
				startActivity(intent);
			}
		});


	}
}
