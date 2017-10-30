package com.zb.verticle;

import com.zb.api.ApiPathConf;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;

/**
 * Created by Administrator on 2017/4/14.
 * http verticle
 */
public class HttpServerVerticle extends AbstractVerticle {


    @Override
    public void start(Future<Void> startFuture) throws Exception {

        Router router = Router.router(vertx);

        ApiPathConf.uploadApi(vertx,router);//加载api

        /**
         * 创建http服务器
         */
        vertx.createHttpServer(new HttpServerOptions().setCompressionSupported(true)).requestHandler(router::accept)
                .listen(config().getInteger("http-port", 6060),
                config().getString("host-name", "0.0.0.0"));

        startFuture.complete();
    }


}
