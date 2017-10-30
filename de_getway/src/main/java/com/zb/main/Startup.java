package com.zb.main;

import com.zb.consumer.ConsumerMessage;
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

        //TODO 配置vertx内部使用的logger类别 4种 default:JULLogDelegateFactory(默认jdk自带日志收集),log4j2,log4j,SLF4J
        System.setProperty("vertx.logger-delegate-factory-class-name","io.vertx.core.logging.Log4j2LogDelegateFactory");

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

        if (null != source) {
            /**部署client verticle*/
//            DeploymentOptions options = new DeploymentOptions().setInstances(Runtime.getRuntime().availableProcessors()*2);
            Vertx vertx = Vertx.vertx();
            vertx.deployVerticle("com.zb.common.verticle.BaseClientVerticle",rs -> {
                if (rs.failed()) {
                    rs.cause().printStackTrace();
                    logger.fatal(">>>>>>>>>Deployment Client failed");
                } else {

                    /**执行message消费消息 **/
                    ConsumerMessage consumerMessage=new ConsumerMessage();
                    consumerMessage.consumer();

                    /**部署httpServer verticle*/
                    vertx.deployVerticle("com.zb.verticle.HttpServerVerticle",ar -> {
                        if(ar.failed()){
                            ar.cause().printStackTrace();
                            logger.fatal(">>>>>>>>>Deployment HttpServer failed");
                        }else{
                            logger.info(">>>>>>>Deployment HttpServer successed");

                        }
                    });

                }
            });
        }
    }
}
