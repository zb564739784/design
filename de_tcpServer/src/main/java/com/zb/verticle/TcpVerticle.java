package com.zb.verticle;

import com.zb.handler.PorcessHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.ClientAuth;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;

/**
 * Created by zhangbo on 17-10-12.
 */
public class TcpVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        //TODO 建立tcp服务器
        String path = TcpVerticle.class.getResource("/jks/server.jks").getPath();
        NetServer server = vertx.createNetServer(new NetServerOptions().setSsl(true)
         .setKeyStoreOptions(new JksOptions().setPath(path).setPassword("123456")));
        PorcessHandler processHandler=new PorcessHandler();
        server.connectHandler(processHandler::process);//连接处理

        server.listen(1234, "0.0.0.0", res -> {
            if (res.succeeded()) {
                System.out.println("Server is now listening!");
                startFuture.complete();
            } else {
                System.out.println("Failed to bind!");
            }
        });

    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        stopFuture.complete();
    }
}
