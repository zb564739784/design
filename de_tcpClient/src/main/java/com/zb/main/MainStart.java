package com.zb.main;

import com.zb.verticle.TcpVerticle;
import io.vertx.core.Vertx;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by zhangbo on 17-10-12.
 */
public class MainStart {

    public static void main(String[] args){
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(TcpVerticle.class.getName(), rs -> {
            if (rs.failed()) {
                rs.cause().printStackTrace();
                System.out.println(">>>>>>>>>Deployment failed");
            } else {
                System.out.println(">>>>>>>>>Deployment success");
            }
        });
    }
}
