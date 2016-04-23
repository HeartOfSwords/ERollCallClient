package com.epoint.dao;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.epoint.properites.ActivityJumpFlag;
import com.epoint.tools.NetTools;

import java.io.IOException;

/**
 * Created by duanjigui on 2016/4/21.
 * 按钮点名实现类
 */
public class ClickRoallcall implements Roallcall {
    @Override
    public void execute(Handler handler) {
        String info=null;
        Message message =new Message();
        try{
            boolean counter= NetTools.GetPersonInformation(ActivityJumpFlag.UserInfo.USER_ID);
            info= NetTools.GetStringFromService(ActivityJumpFlag.URL.CHANGE_OUNTER_URL+ActivityJumpFlag.UserInfo.USER_ID, NetTools.PUT);
            message.obj=info;
            message.arg1= ActivityJumpFlag.HandlerInformation.SIGNED;
            if (counter==true){
                message.arg2=ActivityJumpFlag.IS_COUNTER_SUCCESS;
            }else {
                message.arg2=ActivityJumpFlag.IS_COUNTER_FAIL;
            }
        } catch (IOException e){
            message.arg1=ActivityJumpFlag.HandlerInformation.NETWRONG;
            Log.d("io_error", "未获取输入流！");
        }
        handler.sendMessage(message);
    }
}
