package com.example.erollcall_teacher_module.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.epoint.Activity.R;

/**
 * 获取登录教师信息
 * @author snowalker
 *
 */
public class GetLogedTeacherInfo extends Activity {
	private TextView college;
	private TextView name;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.get_loged_teacher_info);
		college=(TextView)findViewById(R.id.teacher_col);
		name=(TextView)findViewById(R.id.teacher_name);
		Intent intent=getIntent();
		college.setText(intent.getStringExtra("college"));
		name.setText(intent.getStringExtra("name"));
	}


}
