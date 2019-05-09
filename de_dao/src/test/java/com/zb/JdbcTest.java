package com.zb;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhang bo
 * @version 1.0
 * @Description
 * @date 2019-05-09
 */
public class JdbcTest {

    public void exec(){
        Vertx vertx= Vertx.vertx();
        InputStream jdbcIn = JdbcTest.class.getResourceAsStream("/config/jdbc-conf.json");
        String jdbcConf = "";//jdbc连接配置
        JDBCClient jdbcClient = null;
        try {
            jdbcConf = IOUtils.toString(jdbcIn, "UTF-8");//获取配置

            if (!jdbcConf.equals("")) {
                JsonObject json = new JsonObject(jdbcConf);
                jdbcClient = JDBCClient.createShared(vertx, new JsonObject()
                        .put("provider_class","io.vertx.ext.jdbc.spi.impl.HikariCPDataSourceProvider")
                        .put("jdbcUrl", "jdbc:mysql://121.201.57.214/orange_test?useUnicode=true&characterEncoding=UTF-8")
                        .put("driverClassName", "com.mysql.jdbc.Driver")
                        .put("max_pool_size", 30)
                        .put("username", "root")
                        .put("password", "Orangeiot!23"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(null!=jdbcIn)
                try {
                    jdbcIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }


       jdbcClient.getConnection(asyncResult -> {
            if (asyncResult.failed()) {
                asyncResult.cause().printStackTrace();
            } else {
                SQLConnection conn = asyncResult.result();

                conn.query("select * from sys_info",res->{
                    if(res.failed()){
                        res.cause().printStackTrace();
                    }else{
                        System.out.println(res.result().getResults());
                        System.out.println(res.result().getRows());
                        System.out.println(res.result().getNumRows());
                    }
                });
            }
        });

    }

    public static void main(String[] args) {
          new JdbcTest().exec();
    }
}
