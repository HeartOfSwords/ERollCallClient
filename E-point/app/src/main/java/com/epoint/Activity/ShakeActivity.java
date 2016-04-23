package com.epoint.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.epoint.dao.Roallcall;
import com.epoint.dao.ShakeRollcall;
import com.epoint.properites.ActivityJumpFlag;
import com.epoint.tools.ActiveContainer;
import com.epoint.tools.Shakehelper;

public class ShakeActivity extends ActionBarActivity {
    private Shakehelper shakehelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);
        ActiveContainer.addActivity(this);
        shakehelper=new Shakehelper(ShakeActivity.this) {
            @Override
            public void execute_opreation() {
                //创建线程用于处理联网比较费时的任务
                new Thread(){
                    @Override
                    public void run() {
                        Roallcall roallcall=new ShakeRollcall();

                        roallcall.execute(handler);
                    }
                }.start();
            }
        };
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String info=(String)msg.obj;
            int flag=1;//设置标志器，当值为1时表示签到成功，其它值表示签到失败
            Intent intent=new Intent(ShakeActivity.this,StudntActivity.class);
            if (msg.arg1==ActivityJumpFlag.IS_COUNTER_SUCCESS){
                Toast.makeText(ShakeActivity.this,"亲,你已经签过到了！",Toast.LENGTH_SHORT).show();
                shakehelper.stop();
                intent.putExtra("flag",flag);
                ShakeActivity.this.startActivity(intent);
            }else if("failed to set counter".equals(info)&&msg.arg1==ActivityJumpFlag.IS_COUNTER_FAIL){
                Toast.makeText(ShakeActivity.this,"亲，签到失败，请重新签到！",Toast.LENGTH_SHORT).show();
            }else if("网络断了，请连接网络！".equals(info)){
                Toast.makeText(ShakeActivity.this,"亲，网络断了，请连接网络！",Toast.LENGTH_SHORT).show();
            } else if("Successfully reset counter to 1! And that means you are arrived".equals(info)){
                Toast.makeText(ShakeActivity.this,"签到成功！",Toast.LENGTH_SHORT).show();
                shakehelper.stop();
                intent.putExtra("flag",flag);
                ShakeActivity.this.startActivity(intent);
            }
        }
    };

}
