package com.zb.test;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Created by Administrator on 2017/11/1.
 */
public class MqttTest {
    private MqttClient client;
    private String host = "tcp://127.0.0.1:1883";
    private String userName = "admin";
    private String passWord = "password";
    private MqttTopic topic;
    private MqttMessage message;

    private String myTopic = "test/topic";

    public void server() {

        try {
            client = new MqttClient(host, "Server", new MemoryPersistence());
            connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void connect() {

        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setUserName(userName);
        options.setPassword(passWord.toCharArray());
        // 设置超时时间
        options.setConnectionTimeout(10);
        // 设置会话心跳时间
        options.setKeepAliveInterval(20);
        try {
            client.setCallback(new MqttCallback() {

                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("connectionLost-----------");
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    System.out.println("deliveryComplete---------"
                            + token.isComplete());
                }

                @Override
                public void messageArrived(String topic, MqttMessage arg1)
                        throws Exception {
                    System.out.println("messageArrived----------");

                }
            });
            topic = client.getTopic(myTopic);

            message = new MqttMessage();
            message.setQos(1);
            message.setRetained(true);
            System.out.println(message.isRetained() + "------ratained状态");
            message.setPayload("FlyingSnow2211----Forever".getBytes());

            client.connect(options);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args){
        MqttTest mqttTest=new MqttTest();
        mqttTest.server();
    }

}
