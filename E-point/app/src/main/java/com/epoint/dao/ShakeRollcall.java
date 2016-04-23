package com.epoint.dao;

import android.os.Handler;
import android.os.Message;

import com.epoint.properites.ActivityJumpFlag;
import com.epoint.tools.NetTools;

import java.io.IOException;

/**
 * Created by duanjigui on 2016/4/21.
 * 摇一摇点名实现类
 */
public class ShakeRollcall implements Roallcall {
    @Override
    public void execute(Handler handler) {
        Message message=new Message();
        try {
            boolean counter= NetTools.GetPersonInformation(ActivityJumpFlag.UserInfo.USER_ID);
            String info= NetTools.GetStringFromService(ActivityJumpFlag.URL.CHANGE_OUNTER_URL + ActivityJumpFlag.UserInfo.USER_ID, NetTools.PUT);
            message.obj=info;
            if (counter==true){
                message.arg1= ActivityJumpFlag.IS_COUNTER_SUCCESS;
            }else {
                message.arg1=ActivityJumpFlag.IS_COUNTER_FAIL;
            }

        } catch (IOException e) {
            message.obj="网络断了，请连接网络！";
        }
        handler.sendMessage(message);
    }
}
