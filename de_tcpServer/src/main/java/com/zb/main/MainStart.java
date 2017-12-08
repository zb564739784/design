package com.zb.main;

import com.zb.verticle.HttpServerVerticle;
import io.vertx.core.Vertx;

/**
 * Created by zhangbo on 17-10-12.
 */
public class MainStart {

    @SuppressWarnings("Duplicates")
    public static void main(String[] args){
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(HttpServerVerticle.class.getName(),rs -> {
            if (rs.failed()) {
                rs.cause().printStackTrace();
                System.out.println(">>>>>>>>>Deployment failed");
            } else {
                System.out.println(">>>>>>>>>Deployment success");
            }
        });
//        vertx.deployVerticle("com.zb.verticle.TcpVerticle", rs -> {
//            if (rs.failed()) {
//                rs.cause().printStackTrace();
//                System.out.println(">>>>>>>>>Deployment failed");
//            } else {
//                System.out.println(">>>>>>>>>Deployment success");
//            }
//        });
    }
}
