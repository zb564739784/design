package com.zb.handler.LogMessage;

import com.zb.common.constant.KafkaConf;
import com.zb.common.verticle.BaseClientVerticle;
import io.vertx.ext.mail.MailMessage;
import io.vertx.ext.mail.impl.Utils;
import io.vertx.ext.mail.mailencoder.EmailAddress;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by zhangbo on 17-9-28.
 */
public class LogMessageHandler {
    private static Logger logger = LogManager.getLogger(LogMessageHandler.class);


    /**
     * 错误日志邮箱通知
     */
    public void receiving() {

        /**
         * 订阅test topic
         */
        Set<String> topics = new HashSet<>();
        topics.add(KafkaConf.ERROR_LOG);
        BaseClientVerticle.consumer.subscribe(topics);


        /**
         * 订阅处理
         */
        BaseClientVerticle.consumer.handler(record -> {
            logger.info("Processing  topic = {}  ,key = {} ,value = {} ,partition = {} ,offset = {}", record.topic().toString(), record.key(), record.value()
                    , record.partition(), record.offset());

            //TODO 邮箱通知
            BaseClientVerticle.mailClient.sendMail(new MailMessage().setTo("564739784@qq.com")
                    .setFrom("日志报警<no-reply@1sju.com>")
                    .setText(record.value()).setSubject("异常日志报警告"), rs -> {
                if (rs.failed())
                    logger.info(">>>>send email success");
                else
                    logger.info(">>>>send email success");
            });


            /**
             * 手动commit
             */
            BaseClientVerticle.consumer.commit(ar -> {
                if (ar.failed()) {
                    ar.cause().printStackTrace();
                    logger.info(">>>>>>>>>>>>>processing message fail");
                } else {
                    logger.info(">>>>>>>>>>>>>processing message success");
                }
            });
        });

        logger.info("deploy consumer kafka message success");
    }
}
