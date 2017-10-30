package com.zb.common.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.mail.MailClient;
import io.vertx.ext.mail.MailConfig;
import io.vertx.ext.mail.StartTLSOptions;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.kafka.client.consumer.KafkaConsumer;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;
import org.apache.commons.io.IOUtils;
import org.apache.kafka.clients.KafkaClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.JavadocSingleNameReference;

import java.io.*;
import java.util.List;
import java.util.Properties;

/**
 * Created by zhangbo on 17-4-30.
 */
public class BaseClientVerticle extends AbstractVerticle {

    public static JDBCClient jdbcClient;//Jdbc客户端

    public static RedisClient writerRedisClient;//写redis客户端

    public static RedisClient readRedisClient;//读redis客户端

    public static KafkaProducer<String, String> producer;//kafka producer客户端

    private static Logger logger = LogManager.getLogger(BaseClientVerticle.class);

    public static MongoClient mongoClient;//mongo客戶端

    public static Properties properties;//配置文件

    public static WebClient webClient;//WebClient请求

    public static List<String> excludePathList;//过滤排除的url路径集合

    public static KafkaConsumer<String, String> consumer;//kafka消费者

    public static MailClient mailClient;//邮箱服务

    /**
     * 连接sql的配置verticle
     *
     * @param startFuture
     * @throws Exception
     */
    @SuppressWarnings("Duplicates")
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        /**
         * 加载jdbc config
         */
        InputStream jdbcIn = BaseClientVerticle.class.getResourceAsStream("/config/jdbc-conf.json");
        String jdbcConf = "";//jdbc连接配置
        try {
            jdbcConf = IOUtils.toString(jdbcIn, "UTF-8");//获取配置

            if (!jdbcConf.equals("")) {
                JsonObject json = new JsonObject(jdbcConf);
                jdbcClient = JDBCClient.createShared(vertx, json);
            }
        } catch (IOException e) {
            e.printStackTrace();
            startFuture.failed();
        }finally {
            if(null!=jdbcIn)
                jdbcIn.close();
        }


        /**
         * 加载redis config
         */
        InputStream redisIn = BaseClientVerticle.class.getResourceAsStream("/config/redis-conf.json");
        String redisConf = "";//jdbc连接配置
        try {
            redisConf = IOUtils.toString(redisIn, "UTF-8");//获取配置
            if (!redisConf.equals("")) {
                JsonObject json = new JsonObject(redisConf);

                writerRedisClient = RedisClient.create(vertx, new RedisOptions().setHost(json.getString("host"))
                        .setPort(json.getInteger("port")));//创建写redisclient


                readRedisClient = RedisClient.create(vertx, new RedisOptions().setHost(json.getString("host1"))
                        .setPort(json.getInteger("port1")));//创建读redisclient


                /**主从配置*/
                readRedisClient.slaveof(json.getString("host"), json.getInteger("port"), stringAsyncResult -> {
                    if (stringAsyncResult.failed()) {
                        stringAsyncResult.cause().printStackTrace();
                    } else {
                        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>redis slaveof success");
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
            startFuture.failed();
        }finally {
            if(null!=redisIn)
                redisIn.close();
        }


        /**
         * 加载kafka producer config
         */
        InputStream kafkaIn = BaseClientVerticle.class.getResourceAsStream("/config/kafka_producer-conf.properties");
        try {
            Properties config = new Properties();
            config.load(kafkaIn);
            producer = KafkaProducer.create(vertx, config);
        }catch (Exception e){
            e.printStackTrace();
            startFuture.failed();
        }finally {
            if(null!=kafkaIn)
                kafkaIn.close();
        }


        /**
         * 加載mongo配置
         */
        InputStream mongoIn = BaseClientVerticle.class.getResourceAsStream("/config/mongo.json");
        String mongoConf = "";//jdbc连接配置
        try {
            mongoConf = IOUtils.toString(mongoIn, "UTF-8");//获取配置

            if (!mongoConf.equals("")) {
                JsonObject json = new JsonObject(mongoConf);
                mongoClient = MongoClient.createShared(vertx,json);
            }
        }catch (Exception e){
            e.printStackTrace();
            startFuture.failed();
        }finally {
            if(null!=mongoIn)
                mongoIn.close();
        }


        /**
         * 加載conf文件信息
         */
        InputStream confIn = BaseClientVerticle.class.getResourceAsStream("/conf.properties");
        String conf = "";//jdbc连接配置
        try {
            if (null !=confIn) {
                properties=new Properties();
                properties.load(confIn);
            }
        }catch (Exception e){
            e.printStackTrace();
            startFuture.failed();
        }finally {
            if(null!=confIn)
                confIn.close();
        }

        /**
         * 加載webClient配置
         */
        InputStream webClientIn = BaseClientVerticle.class.getResourceAsStream("/config/elasticsearch.json");
        String webCiientConf = "";//jdbc连接配置
        try {
            webCiientConf = IOUtils.toString(webClientIn, "UTF-8");//获取配置

            if (!webCiientConf.equals("")) {
                JsonObject json = new JsonObject(webCiientConf);
                webClient = WebClient.create(vertx,new WebClientOptions().setConnectTimeout(json.getInteger("timeout"))
                        .setDefaultPort(json.getInteger("port")).setMaxPoolSize(json.getInteger("maxPoolSize"))
                        .setDefaultHost(json.getString("host")));
            }
        }catch (Exception e){
            e.printStackTrace();
            startFuture.failed();
        }finally {
            if(null!=webClientIn)
                webClientIn.close();
        }


        /**
         * 加载过滤排除的url路径集合
         */
        InputStream excludePathIn = BaseClientVerticle.class.getResourceAsStream("/config/filterPath.json");
        String excludePathConf = "";//jdbc连接配置
        try {
            excludePathConf = IOUtils.toString(excludePathIn, "UTF-8");//获取配置

            if (!excludePathConf.equals("")) {
                JsonObject json = new JsonObject(excludePathConf);
                excludePathList=json.getJsonArray("exclude_path").getList();
            }
        }catch (Exception e){
            e.printStackTrace();
            startFuture.failed();
        }finally {
            if(null!=excludePathIn)
                excludePathIn.close();
        }


        /**
         * 加载kafka消费者
         */
        InputStream consumerkfkIn = BaseClientVerticle.class.getResourceAsStream("/config/kafka_consumer-conf.properties");
        try {
            Properties kfkconfig = new Properties();
            kfkconfig.load(consumerkfkIn);
            consumer = KafkaConsumer.create(vertx, kfkconfig);

        }catch (Exception e){
            e.printStackTrace();
            startFuture.failed();
        }finally {
            if(null!=excludePathIn)
                excludePathIn.close();
        }


        /**
         * 配置邮箱服务
         */
        InputStream mainIn = BaseClientVerticle.class.getResourceAsStream("/config/email-config.properties");
        try {
            Properties mainConfig = new Properties();
            mainConfig.load(mainIn);

            MailConfig config = new MailConfig();
            config.setHostname(mainConfig.getProperty("mailServerHost"));
            config.setPort(Integer.parseInt(mainConfig.getProperty("mailServerPort")));
            config.setStarttls(StartTLSOptions.REQUIRED);
            config.setUsername(mainConfig.getProperty("userName"));
            config.setPassword(mainConfig.getProperty("password"));
            config.setMaxPoolSize(100);
            mailClient = MailClient.createNonShared(vertx, config);
        }catch (Exception e){
            e.printStackTrace();
            startFuture.failed();
        }finally {
            if(null!=excludePathIn)
                excludePathIn.close();
        }


        startFuture.complete();
    }

}

















