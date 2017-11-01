package com.zb.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.concurrent.CompletableFuture;

/**
 * Created by Administrator on 2017/10/15.
 */
public class HttpServerVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        Router router = Router.router(vertx);


        router.route().handler(BodyHandler.create());

        router.post("/test").handler(routingContext -> {
            System.out.println("params="+routingContext.getBody());
            CompletableFuture.runAsync(()->MQTTServerVerticle.sendMessage(routingContext.getBodyAsString()));
            routingContext.response().end("{\"msg\":\"ok\"}");
        });//测试


        vertx.createHttpServer()
//                new HttpServerOptions().setSsl(true).setKeyStoreOptions(new JksOptions().setPath(serverPath)
//                   .setPassword("123456")).setTrustStoreOptions(new JksOptions().setPath(clientPath).setPassword("123456")))
                .requestHandler(router::accept).listen(config().getInteger("http-port", 8081),
                config().getString("host-name", "0.0.0.0"));


        startFuture.complete();
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        stopFuture.complete();
    }
}
