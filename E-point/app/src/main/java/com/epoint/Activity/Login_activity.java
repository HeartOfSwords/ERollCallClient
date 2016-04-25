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
import com.snowalker.erollcall.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author lipengpeng
 */
public class Login_activity extends Activity {

    //获取登录界面相关参数
    private EditText user_name; //获取用户名
    private EditText user_pwd;  //获取密码
    private RadioGroup radioGroup;
    private RadioButton user_type;  //获取用户身份
    private Button login_button;    //获取登录按钮

    //获取从子线程中传递过来的参数，执行不同的操作
    //跳转学生菜单页
    //跳转教师菜单页
    //身份验证未通过，弹出登录失败信息
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            String result = (String) msg.obj;

            if (msg.arg1 == ActivityJumpFlag.UserInfo.STUDENT) {
                Toast.makeText(Login_activity.this, result, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login_activity.this, StudntActivity.class);
                Login_activity.this.startActivity(intent);
            } else if (msg.arg1 == ActivityJumpFlag.UserInfo.TEACHER) {
                Toast.makeText(Login_activity.this, result, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login_activity.this, TeacherResetCounter.class);
                Login_activity.this.startActivity(intent);
            } else {
                Toast.makeText(Login_activity.this, "登陆失败！", Toast.LENGTH_SHORT).show();
            }

        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_activity);

        //将login_activity.java加入活动列表List
        ActiveContainer.addActivity(Login_activity.this);

        user_name = (EditText) findViewById(R.id.user_name);
        user_pwd = (EditText) findViewById(R.id.user_pwd);
        radioGroup = (RadioGroup) findViewById(R.id.user_type);

        login_button = (Button) findViewById(R.id.login_button);

        //为登录按钮设置监听器
        login_button.setOnClickListener(new OnClickListener() {

            //设置获取登录验证返回结果的标志变量
            private String result;

            @Override
            public void onClick(View arg0) {
                user_type = (RadioButton) findViewById(radioGroup
                        .getCheckedRadioButtonId());
                new Thread() {
                    public void run() {
                        String username = null;
                        Message message = new Message();
                        try {

                            //解决用户名为汉字时的乱码问题，使用URLEncode.encode方法
                            username = URLEncoder.encode(user_name.getText().toString(), "utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        String userpwd = user_pwd.getText().toString();
                        String usertype = user_type.getText().toString();

                        //当用户名和密码为空时执行相关操作，似乎已经没用了，暂留
                        if (username.trim().equals("") || userpwd.trim().equals("")) {

                        } else if (usertype.equals("学生")) {  //如果登录用户学生为身份，向学生验证接口发送请求
                            result = LoginTools.GetLoginResult(ActivityJumpFlag.URL.LOGIN_VERIFY + "student" + "/" + username + "/" + userpwd, NetTools.GET);
                            if ("登陆成功".equals(result)) {  //身份验证通过
                                message.arg1 = ActivityJumpFlag.UserInfo.STUDENT;   //将学生验证通过标志变量赋值给arg1，方便在handler中进行比较
                                ActivityJumpFlag.UserInfo.USER_ID = user_name.getText().toString(); //将当前用户的用户名赋值给一个公共静态变量储存
                            } else {

                            }
                        } else {
                            result = LoginTools.GetLoginResult(ActivityJumpFlag.URL.LOGIN_VERIFY + "teacher" + "/" + username + "/" + userpwd, NetTools.GET);
                            if ("登陆成功".equals(result)) {    //教师登录成功
                                message.arg1 = ActivityJumpFlag.UserInfo.TEACHER;
                                ActivityJumpFlag.UserInfo.USER_ID = user_name.getText().toString();
                            } else {

                            }
                        }
                        message.obj = result;   //将返回的结果传递给主线程
                        handler.sendMessage(message);
                    }

                    ;
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