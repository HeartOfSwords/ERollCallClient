package com.epoint.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.epoint.dao.ClickRoallcall;
import com.epoint.dao.Roallcall;
import com.epoint.properites.ActivityJumpFlag;
import com.epoint.tools.ActiveContainer;
import com.epoint.tools.JsonPaser;
import com.epoint.tools.NetTools;
import com.epoint.tools.StorgeFlag;
import com.example.erollcall_teacher_module.activity.AboutUsActivity;
import com.snowalker.erollcall.R;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class StudntActivity extends ActionBarActivity implements View.OnLongClickListener{
    private Button sign;//查看个人信息按钮
    private Button Shake;
    private Button sign_flag;//签到按钮
    private Button jumpToAboutUs; //关于我们按钮
    private List<Map<String,Object>> list=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActiveContainer.addActivity(this);//用来将该活动加入到活动会收池中
        sign=(Button)findViewById(R.id.sign);
        Shake=(Button)findViewById(R.id.Shake);
        sign_flag=(Button)findViewById(R.id.sign_flag);

        //获取关于我们按钮——李鹏鹏
        jumpToAboutUs = (Button)findViewById(R.id.about_us);
        jumpToAboutUs.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudntActivity.this,
                        AboutUsActivity.class);
                startActivity(intent);
            }
        });

        StorgeFlag readstorge=new StorgeFlag() {
            @Override
            public void unlock() {
                sign_flag.setEnabled(true);
                Shake.setEnabled(true);
            }
            @Override
            public void lock() {
                sign_flag.setEnabled(false);//锁定按钮
                Shake.setEnabled(false);//锁定按钮
            }
        };
        readstorge.ReadInfo(StudntActivity.this);
        Intent from_shaked_intent=getIntent();//获取从ShakeActivity传来的信息
        if (from_shaked_intent.getIntExtra("flag",0)==1){
            ActivityJumpFlag.JumpFlag=true;//重新设置标志位
            sign_flag.setEnabled(false);//锁定按钮
            Shake.setEnabled(false);//锁定按钮
        }
        sign.setOnLongClickListener(this);
        Shake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(StudntActivity.this,ShakeActivity.class);
                StudntActivity.this.startActivity(intent);
            }
        });
    }
    Handler handler=new Handler(){
        //将子线程的时间放到主线程中处理
        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1== ActivityJumpFlag.HandlerInformation.SIGNED){ //当点击签到按钮时
                String s=(String) msg.obj;
                if (msg.arg2== ActivityJumpFlag.IS_COUNTER_SUCCESS){
                    Toast.makeText(StudntActivity.this, "亲,你已经签过到了！", Toast.LENGTH_SHORT).show();
                    sign_flag.setEnabled(false);
                    Shake.setEnabled(false);
                    ActivityJumpFlag.JumpFlag=true;
                }else if("failed to set counter".equals(s)&&msg.arg2== ActivityJumpFlag.IS_COUNTER_FAIL){
                    Toast.makeText(StudntActivity.this, "亲，签到失败，请重新签到！", Toast.LENGTH_SHORT).show();
                }else if("Successfully reset counter to 1! And that means you are arrived".equals(s)){
                    ActivityJumpFlag.JumpFlag=true;//设置标志位，已经签过到了
                    Toast.makeText(StudntActivity.this, "签到成功！", Toast.LENGTH_SHORT).show();
                    sign_flag.setEnabled(false);
                    Shake.setEnabled(false);
                }else{
                    Toast.makeText(StudntActivity.this, "签到失败！", Toast.LENGTH_SHORT).show();
                }
            }else if (msg.arg1== ActivityJumpFlag.HandlerInformation.VIEWSELF){  //当点击查看自己信息时
                String info=(String)msg.obj;
                if (info!=null){
                    JsonPaser paser=new JsonPaser();
                    try {
                        list=  paser.PaserJson(info, JsonPaser.ARRARY);
                        for (Map<String,Object> temp:list){
                            Intent intent =new Intent(StudntActivity.this,StudentInfoActivity.class);
                            for (String s:temp.keySet()){
                                intent.putExtra(s,(String)temp.get(s));
                            }
                            StudntActivity.this.startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("paser_error", "解析json出错！");
                    }
                }
            }else if (msg.arg1== ActivityJumpFlag.HandlerInformation.NETWRONG){ //当没有联网时
                Toast.makeText(StudntActivity.this, "亲，网络断了，请连接网络！", Toast.LENGTH_SHORT).show();
            }

        }
    };

    /**
     * 处理签到按钮事件
     * @param view
     */
    public void Signed(View view){
        new Thread(){
            @Override
            public void run() {
                Roallcall roallcall=new ClickRoallcall();
                roallcall.execute(handler);
            }
        }.start();
    }
    /**
     *  处理显示学生信息
     * @param view
     */
    public void ViewInfo(View view){
        new Thread(){
            @Override
            public void run() {
                Message message =new Message();
                String info=null;
                try {
                    info = NetTools.GetStringFromService(ActivityJumpFlag.URL.QUERY_STUDENT_SELF_INFOR + ActivityJumpFlag.UserInfo.USER_ID, NetTools.GET);
                    message.obj=info;
                    message.arg1= ActivityJumpFlag.HandlerInformation.VIEWSELF;
                } catch (IOException e){
                    message.arg1= ActivityJumpFlag.HandlerInformation.NETWRONG;
                    Log.d("io_error", "未获取输入流！");
                }
                handler.sendMessage(message);
            }
        }.start();
    }
    /**
     * 长点击签到实现学生签到
     * @param view
     * @return
     */
    @Override
    public boolean onLongClick(View view) {
        Signed(view);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==4){
            if (ActivityJumpFlag.JumpFlag==true){
                StorgeFlag storgeFlag=new StorgeFlag() {
                    @Override
                    public void unlock() {

                    }

                    @Override
                    public void lock() {

                    }
                };//用于将状态信息存储到本地数据
                storgeFlag.WriteInfo(StudntActivity.this, ActivityJumpFlag.JumpFlag, System.currentTimeMillis());
            }
            ActiveContainer.close();
        }
        return super.onKeyDown(keyCode, event);
    }

}
