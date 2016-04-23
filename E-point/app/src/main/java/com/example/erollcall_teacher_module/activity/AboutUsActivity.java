package com.example.erollcall_teacher_module.activity;

import android.os.Bundle;
import android.view.Window;
import android.app.Activity;

import com.epoint.Activity.R;

/**
 * 关于我们
 * @author snowalker
 *
 */
public class AboutUsActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.about_us_layout);
		new  Thread(){}.start();
	}
}