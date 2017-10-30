package com.zb.consumer.message;

import com.zb.consumer.verticle.KafkaConsumerVerticle;
import io.vertx.kafka.client.common.TopicPartition;
import io.vertx.kafka.client.consumer.KafkaConsumerRecord;
import io.vertx.kafka.client.consumer.impl.KafkaConsumerRecordImpl;
import org.apache.kafka.clients.KafkaClient;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.record.TimestampType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by zhangbo on 17-4-30.
 */
@SuppressWarnings("Duplicates")
public class ReceTestMassage {

    private static Logger logger = LogManager.getLogger(ReceTestMassage.class);

    public void receiving() {



        /**
         * 订阅处理
         */
        KafkaConsumerVerticle.consumer.handler(record -> {
            logger.info("Processing  topic="+record.topic().toString()+" key=" + record.key() + ",value=" + record.value() +
                    ",partition=" + record.partition() + ",offset=" + record.offset());

            /**
             * 手动commit
             */
            KafkaConsumerVerticle.consumer.commit(ar->{
                if(ar.failed()){
                    ar.cause().printStackTrace();
                    logger.info(">>>>>>>>>>>>>processing message fail");
                }else{
                    logger.info(">>>>>>>>>>>>>processing message success");
                }
            });
        });

        /**
         * 订阅test topic
         */
//        Set<String> topics = new HashSet<>();
//        topics.add("my-T");
//        topics.add("test");
//        KafkaConsumerVerticle.consumer.subscribe(topics);


        /**
         * 订阅topic 特殊 partition
         */
        Set<TopicPartition> topicPartitions=new HashSet<>();
        for(int i=0;i<10;i++){
            topicPartitions.add(new TopicPartition().setTopic("test").setPartition(i));
            topicPartitions.add(new TopicPartition().setTopic("my-T").setPartition(i));
        }
        KafkaConsumerVerticle.consumer.assign(topicPartitions);

    }
}
