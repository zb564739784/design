package com.zb.verticle;

import com.zb.handler.PorcessHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.ClientAuth;
import io.vertx.core.net.*;

/**
 * Created by zhangbo on 17-10-12.
 */
public class TcpVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        //TODO 建立tcp服务器
        String path = TcpVerticle.class.getResource("/jks/client.jks").getPath();
        NetClientOptions options = new NetClientOptions().setConnectTimeout(10000)
              .setSsl(true).setTrustStoreOptions(new JksOptions().setPath(path).setPassword("123456"));//连接超时
        options.setReconnectAttempts(10);//重连次数
        options.setReconnectInterval(500);//每次重连的时间间隔
        NetClient client = vertx.createNetClient(options);
        client.connect(1234, "0.0.0.0", res -> {
            if (res.succeeded()) {
                System.out.println("Connected!");
                // TODO: 17-10-12 处理
                PorcessHandler processHandler = new PorcessHandler();
                processHandler.process(res.result());
            } else {
                System.out.println("Failed to connect: " + res.cause().getMessage());
            }
        });
        startFuture.complete();
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        stopFuture.complete();
    }
}
