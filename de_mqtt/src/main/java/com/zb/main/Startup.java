package com.zb.main;

import com.zb.verticle.HttpServerVerticle;
import com.zb.verticle.MQTTServerVerticle;
import io.vertx.core.Vertx;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * Created by Administrator on 2017/4/14.
 */
public class Startup {
    private static Logger logger = LogManager.getLogger(Startup.class);

    @SuppressWarnings("Duplicates")
    public static void main(String[] args) {
                     /**部署httpServer verticle*/
        Vertx.vertx().deployVerticle(MQTTServerVerticle.class.getName(), ar -> {
                        if(ar.failed()){
                            ar.cause().printStackTrace();
                            logger.fatal(">>>>>>>>>Deployment HttpServer failed");
                        }else{
                            logger.info(">>>>>>>Deployment HttpServer successed");

                        }
                    });
        Vertx.vertx().deployVerticle(HttpServerVerticle.class.getName());

    }
}
