package com.zb.common.general;

/**
 * Created by zhangbo on 17-7-4.
 */
public enum  ErrorType {
    RESULT_LOGIN_NO(700,"没有登录"),//没有登录
    RESULT_LOGIN_EMPTY(100, "账号或密码为空"),//登录参数有空值
    RESULT_LOGIN_FIAL(101, "账号或密码错误"),   //登录失败
    RESULT_LOGIN_ILLEGAL(806, "非法数据"),   //数据格式不正确
    RESULT_LOGIN_EVIL(801, "恶意登录"),   //恶意登录
    RESULT_SERVER_FIAL(500, "服务器出小差"),   //服务起出错
    RESULT_RESOURCES_NOT_FIND(404, "资源未找到"),   //资源未找到
    RESULT_UNKONWN(999, "资源未找到"),   //未知异常
    RESULT_LOG_OUT_FIAL(106, "没有登录"),   //未知异常
    RESULT_SERVER_TIME_OUT(509, "服務請求處理超時"),   //處理超時
    RESULT_DATA_FAIL(400, "请求数据或或格式不对"),   //请求数据或或格式不对
    RESULT_PARAMS_FAIL(401, "数据参数不对"),   //必需的参数没有传或参数类型不对null值
    RESULT_CODE_NOREQUIRED(406, "不需要显示验证码"),   //不需要显示验证码
    RESULT_CODE_FAIL(408, "验证码错误");   //验证码错误

    private int key;

    private String value;

    ErrorType(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }




}
