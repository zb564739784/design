package com.zb.consumer.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.kafka.client.consumer.KafkaConsumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by zhangbo on 17-4-30.
 */
public class KafkaConsumerVerticle  extends AbstractVerticle{

    private static Logger logger = LogManager.getLogger(KafkaConsumerVerticle.class);

    public static KafkaConsumer<String, String> consumer;

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        InputStream kafkaIn = KafkaConsumerVerticle.class.getResourceAsStream("/config/kafka_consumer-conf.properties");
        try {
            Properties config = new Properties();
            config.load(kafkaIn);
            consumer = KafkaConsumer.create(vertx, config);

            startFuture.complete();
        }catch (Exception e){
            e.printStackTrace();
            startFuture.failed();
        }

    }
}
