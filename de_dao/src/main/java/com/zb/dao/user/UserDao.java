package com.zb.dao.user;

import com.zb.common.verticle.BaseClientVerticle;
import com.zb.sql.user.UserDaoSql;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2017/4/14.
 */
public class UserDao {

    /**
     * 登錄
     * @param username 賬戶名
     * @param pwd 密碼
     * @param handler 異步處理返回
     */
//    public void login(String username, String pwd, Handler<AsyncResult> handler){
//        BaseClientVerticle.jdbcClient.getConnection(asyncResult->{
//            if(asyncResult.failed()){
//                asyncResult.cause().printStackTrace();
//            }else{
//                SQLConnection conn=asyncResult.result();
//                conn.queryWithParams(UserDaoSql.LOGIN_QUERY,new JsonArray().add(username).add(pwd),rs->{
//                    if(rs.failed()){
//                        handler.handle(Future.failedFuture(rs.cause().getMessage()));
//                    }else{
//                        handler.handle(Future.succeededFuture(rs.result().getResults().size()));
//                    }
//
//                }).close();
//            }
//        });
//    }


    /**
     * 登錄
     *
     * @param username 賬戶名
     * @param pwd      密碼
     * @param handler  異步處理返回
     */
    public void login(String username, String pwd, Handler<AsyncResult> handler) {
        BaseClientVerticle.mongoClient.findOne("user", new JsonObject().put("username", username), new JsonObject()
                        .put("phone", "").put("address", "").put("is_delete", "").put("nickname", "").put("salt", "").put("password", ""),
                res -> {
                    if (res.failed()) {
                        res.cause().printStackTrace();
                    } else {
                        handler.handle(Future.succeededFuture(res.result()));
                    }
                });
    }


    /**
     * 测试
     * @param handler
     */
    public void test(Handler<AsyncResult> handler) {
        BaseClientVerticle.jdbcClient.getConnection(asyncResult -> {
            if (asyncResult.failed()) {
                asyncResult.cause().printStackTrace();
            } else {
                SQLConnection conn = asyncResult.result();
                conn.query(UserDaoSql.TTEST_QUERY, rs -> {
                    if (rs.failed()) {
                        handler.handle(Future.failedFuture(rs.cause().getMessage()));
                    } else {
                        List<Map<String,Object>> mapList = new ArrayList<>();
                        rs.result().getRows().stream().collect(Collectors.groupingBy(e->e.remove("department_id"), Collectors.groupingBy(c->c.remove("department_name"))))
                                .forEach((k,v) -> {
                                    Map<String,Object> mapcc = new HashMap<>();
                                    mapcc.put("department_id", k);
                                    v.forEach((k2,v2)->{
                                        mapcc.put("department_name", k2);
                                        List<Map<String,Object>> res = new ArrayList<>();
                                        v2.forEach(mm -> {
                                            Map<String,Object> m = new HashMap<>();
                                            m.put("user_id", mm.getValue("user_id"));
                                            m.put("nickname",  mm.getValue("nickname"));
                                            m.put("insert_time",  mm.getValue("insert_time"));
                                            res.add(m);
                                        });
                                        mapcc.put("users", res);
                                    });
                                    mapList.add(mapcc);
                                });
                        handler.handle(Future.succeededFuture(mapList));

                        handler.handle(Future.succeededFuture(mapList));
                    }

                }).close();
            }
        });
    }
}
