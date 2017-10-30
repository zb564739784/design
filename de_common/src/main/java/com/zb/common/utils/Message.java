package com.zb.common.utils;

import com.zb.common.verticle.BaseClientVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import io.vertx.kafka.client.producer.RecordMetadata;

/**
 * Created by zhangbo on 17-4-30.
 */
public class Message {

    public static void send(JsonObject jsonObject){

            // only topic and message value are specified, round robin on destination partitions
            KafkaProducerRecord<String, String> record =
                    KafkaProducerRecord.create("test", null,jsonObject.toString(),6);

            BaseClientVerticle.producer.write(record, done -> {

                if (done.succeeded()) {

                    RecordMetadata recordMetadata = done.result();
                    System.out.println("Message " + record.value() + " written on topic=" + recordMetadata.getTopic() +
                            ", partition=" + recordMetadata.getPartition() +
                            ", offset=" + recordMetadata.getOffset());
                }

            });


            // only topic and message value are specified, round robin on destination partitions
            KafkaProducerRecord<String, String> records =
                    KafkaProducerRecord.create("my-T", null,"my name's "+jsonObject.getString("username"),9);

            BaseClientVerticle.producer.write(records, done -> {

                if (done.succeeded()) {

                    RecordMetadata recordMetadata = done.result();
                    System.out.println("Message " + record.value() + " written on topic=" + recordMetadata.getTopic() +
                            ", partition=" + recordMetadata.getPartition() +
                            ", offset=" + recordMetadata.getOffset());
                }

            });
    }
}
