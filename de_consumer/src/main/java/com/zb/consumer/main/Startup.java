package com.zb.consumer.main;

import com.zb.consumer.message.ReceTestMassage;
import io.vertx.core.Vertx;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.InputStream;

/**
 * Created by Administrator on 2017/4/14.
 */
public class Startup {
    private static Logger logger = LogManager.getLogger(Startup.class);

    @SuppressWarnings("Duplicates")
    public static void main(String[] args) {
        /**加载log4j2配置*/
        ConfigurationSource source = null;
        try {
            //加载log4j2配置
            InputStream in = Startup.class.getResourceAsStream("/log4j2.xml");
            source = new ConfigurationSource(in);
            Configurator.initialize(null, source);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle("com.zb.consumer.verticle.KafkaConsumerVerticle", ar -> {
            if (ar.failed()) {
                ar.cause().printStackTrace();
                logger.info("deploy verticle fail");
            } else {
                ReceTestMassage receTestMassage=new ReceTestMassage();
                receTestMassage.receiving();
                logger.info("deploy verticle success");
            }
        });
    }
}
