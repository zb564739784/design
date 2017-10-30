package com.zb.sql.user;

/**
 * Created by Administrator on 2017/4/14.
 */
public interface UserDaoSql {

    /**登录*/
    public static final String LOGIN_QUERY="select id from sys_user where login_name=? and password=? limit 1";



    /**测试数据*/
    public static final String TTEST_QUERY=" select sd.id department_id,sd.name department_name,su.id user_id" +
            ",su.nickname nickname,su.insert_time insert_time from sys_user su LEFT JOIN  sys_department  sd on sd.id=su.department_id";

}
