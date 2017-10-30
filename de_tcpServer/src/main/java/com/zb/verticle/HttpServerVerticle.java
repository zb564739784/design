package com.zb.verticle;

import com.zb.handler.PorcessHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.SelfSignedCertificate;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;
import sun.java2d.loops.ProcessPath;

/**
 * Created by Administrator on 2017/10/15.
 */
public class HttpServerVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        Router router = Router.router(vertx);

        router.get("/test").handler(routingContext -> {
            PorcessHandler.netSocketList.get(0).write("{\"msg\":\"hahahaha\"}");
            routingContext.response().end("{\"msg\":\"ok\"}");
        });//测试

        router.get("/hello").handler(routingContext -> {
            routingContext.response().end("{\"msg\":\"ok\"}");
        });//测试

        String serverPath = TcpVerticle.class.getResource("/jks/server.jks").getPath();
        String clientPath = TcpVerticle.class.getResource("/jks/client.jks").getPath();
        vertx.createHttpServer()
//                new HttpServerOptions().setSsl(true).setKeyStoreOptions(new JksOptions().setPath(serverPath)
//                   .setPassword("123456")).setTrustStoreOptions(new JksOptions().setPath(clientPath).setPassword("123456")))
                .requestHandler(router::accept).listen(config().getInteger("http-port", 8090),
                config().getString("host-name", "0.0.0.0"));


        SockJSHandlerOptions options = new SockJSHandlerOptions().setHeartbeatInterval(2000);

        SockJSHandler sockJSHandler = SockJSHandler.create(vertx, options);

        sockJSHandler.socketHandler(sockJSSocket -> {
            // Just echo the data back
            sockJSSocket.handler(buffer->{
                System.out.println("======>socket===:"+new String(buffer.getBytes()));
            });
        });


        router.route("/myapp/*").handler(sockJSHandler);

        startFuture.complete();
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        stopFuture.complete();
    }
}
