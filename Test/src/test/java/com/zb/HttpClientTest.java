package com.zb;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;
import io.vertx.core.file.impl.FileSystemImpl;
import io.vertx.core.impl.StringEscapeUtils;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

import java.net.URLEncoder;

/**
 * Created by Administrator on 2017/5/7.
 */
public class HttpClientTest extends AbstractVerticle{


    @Override
    public void start(Future<Void> startFuture) throws Exception {
        WebClient client = WebClient.create(vertx,new WebClientOptions().setSsl(true).setConnectTimeout(3000)
        .setDefaultPort(443).setMaxPoolSize(100).setDefaultHost("api.weixin.qq.com"));
        for(int i=0;i<100;i++) {
            int finalI = i;
            client.post("/cgi-bin/menu/create")
                    .addQueryParam("access_token","ACCESS_TOKEN")
                    .sendJson(new JsonObject("{\"cc\":\"asd\"}"), ar -> {
                        if (ar.succeeded()) {
                            // Obtain response
                            HttpResponse<Buffer> response = ar.result();

                            System.out.println(">>>>>>"+ finalI +">>>>>>>code:"+response.statusCode()+">>>>>>message:" + response.bodyAsString());
                        } else {
                            System.out.println(">>>>>>"+ finalI +">>>>>>>>>>>>>Something went wrong:" + ar.cause().getMessage());
                        }
                    });
        }
    }



    public static void main(String[] args){
        Vertx vertx= Vertx.vertx();
        vertx.deployVerticle(HttpClientTest.class.getName(),asyncResult -> {
            System.out.println(">>>>>>>>>>..deploy successed");
        });

    }


}
