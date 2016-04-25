package com.epoint.properites;

/**
 * Created by duanjigui on 2016/4/16.
 * 此类用来放一些常量值，有的是用来锁定按钮，防止重复提交，有的是一些传递的标志位。
 */
public class ActivityJumpFlag {

    public  static  Boolean JumpFlag=false; // 是否签到，默认值为未签

    public static int IS_COUNTER_SUCCESS =500; //表示已签到

    public static int IS_COUNTER_FAIL =400; //表示已签到

    public static class HandlerInformation{  //表示在MainActivity活动中需要用到的信息的跳转标志

        public static final int SIGNED=1;//签到

        public static final int VIEWSELF=2;//获取信息

        public static final int NETWRONG=3;//网络错误标示
    }

    public static class URL{ //封装一些常见的url地址，方便使用

        public static final String CHANGE_OUNTER_URL="http://119.29.3.138:8080/eback/webapi/students/counter/changeone/";  //更改学生签到状态的url

        public static final String QUERY_STUDENT_ALL_INFOR="http://119.29.3.138:8080/eback/webapi/teachers/students/all";  //查询所有学生信息的url

        public static final String QUERY_STUDENT_SELF_INFOR="http://119.29.3.138:8080/eback/webapi/students/";  //查看指定学生的信息的url

        public static final String LOGIN_VERIFY="http://119.29.3.138:8080/eback/webapi/loginValidate/"; //登录验证
    }

    public static class UserInfo{ //封装一些信息（学生和教师的）

        public static String USER_ID=null;  //传递用户的id主键

        public static final int STUDENT=777;

        public static final int TEACHER=888;
    }
}
