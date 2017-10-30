package com.zb.common.constant;

/**
 * Created by zhangbo on 17-6-19.
 */
public interface ApiConf {


    /**
     * api路径
     */
    public static String USERLOGIN="/login";//用户登录

    public static String USERLOGOUT="/logout";//用户登录

    public static String LOGINCODE="/loginCode";//登錄驗證碼

    public static String ISREQUIREDCODE="/isLoginCode";//是否显示验证码

    public static String SELECTLOGPAGE="/log/selectLogPage";//查看日志列表

    public static String DOWNFILE="/file/down";//文件下载

    public static String UPLOAD="/file/upload";//文件上载

    public static String TEST="/test";//测试

    public static String INDEX="/index";//主頁

    public static String ROUTE="/";//路由入口

}
