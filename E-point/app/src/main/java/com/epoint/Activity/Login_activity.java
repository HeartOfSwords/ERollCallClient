package com.epoint.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.epoint.properites.ActivityJumpFlag;
import com.epoint.tools.ActiveContainer;
import com.epoint.tools.LoginTools;
import com.epoint.tools.NetTools;
import com.example.erollcall_teacher_module.activity.TeacherResetCounter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
/**
 * @author lipengpeng
 *
 */
public class Login_activity extends Activity {

    private EditText user_name;
    private EditText user_pwd;
    private RadioGroup radioGroup;
    private RadioButton user_type;
    private Button login_button;


    Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            String result = (String) msg.obj;

            if (msg.arg1==ActivityJumpFlag.UserInfo.STUDENT){
                Toast.makeText(Login_activity.this, result, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(Login_activity.this,StudntActivity.class);
                Login_activity.this.startActivity(intent);
            }else if (msg.arg1==ActivityJumpFlag.UserInfo.TEACHER){
                Toast.makeText(Login_activity.this, result, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(Login_activity.this,TeacherResetCounter.class);
                Login_activity.this.startActivity(intent);
            }else {
                Toast.makeText(Login_activity.this, "登陆失败！", Toast.LENGTH_SHORT).show();
            }

        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_activity);

        ActiveContainer.addActivity(Login_activity.this);

        user_name = (EditText) findViewById(R.id.user_name);
        user_pwd = (EditText) findViewById(R.id.user_pwd);
        radioGroup = (RadioGroup) findViewById(R.id.user_type);

        login_button = (Button) findViewById(R.id.login_button);

        login_button.setOnClickListener(new OnClickListener() {

            private String result;

            @Override
            public void onClick(View arg0) {
                user_type = (RadioButton) findViewById(radioGroup
                        .getCheckedRadioButtonId());
                new Thread(){
                    public void run() {
                        String username = null;
                        Message message = new Message();
                        try {
                            username = URLEncoder.encode(user_name.getText().toString(),"utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        String userpwd = user_pwd.getText().toString();
                        String usertype = user_type.getText().toString();

                        if (username.trim().equals("") || userpwd.trim().equals("")){

                        }else if(usertype.equals("学生")){
                            result = LoginTools.GetLoginResult(ActivityJumpFlag.URL.LOGIN_VERIFY+"student"+"/"+username+"/"+userpwd, NetTools.GET);
                            if ("登陆成功".equals(result)) {
                                message.arg1=ActivityJumpFlag.UserInfo.STUDENT;
                                ActivityJumpFlag.UserInfo.USER_ID=user_name.getText().toString();
                            }else{

                            }
                        }else{
                            result = LoginTools.GetLoginResult(ActivityJumpFlag.URL.LOGIN_VERIFY + "teacher" + "/" + username + "/" + userpwd, NetTools.GET);
                            if ("登陆成功".equals(result)) {
                                message.arg1=ActivityJumpFlag.UserInfo.TEACHER;
                                ActivityJumpFlag.UserInfo.USER_ID=user_name.getText().toString();
                            }else{

                            }
                        }
                        message.obj = result;
                        handler.sendMessage(message);
                    };
                }.start();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}