package com.zb;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Cookie;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.FaviconHandler;
import io.vertx.ext.web.templ.ThymeleafTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/10.
 */
public class UserController  extends AbstractVerticle  {

    @Override
    public void start() throws Exception {
        // To simplify the development of the web components we use a Router to route all HTTP requests
        // to organize our code in a reusable way.
        final Router router = Router.router(vertx);

        // In order to use a Thymeleaf template we first need to create an engine
        final ThymeleafTemplateEngine engine = ThymeleafTemplateEngine.create();

        // This cookie handler will be called for all routes
        router.route().handler(CookieHandler.create());
//        router.route().handler(FaviconHandler.create("images/favicon.jpeg"));


        // Entry point to the application, this will render a custom JADE template.
        router.route("/index").handler(ctx -> {
            Cookie someCookie = ctx.getCookie("sessionId");

            if (someCookie == null) {
                // Add a cookie - this will get written back in the response automatically
                ctx.addCookie(Cookie.cookie("sessionId", "q123123"));
            }

            // we define a hardcoded title for our application
            ctx.put("welcome", "Hi there!");

            List<Map> uList=new ArrayList<>();
            Map<String,String> map=new HashMap<>();
            map.put("userName","i死");
            map.put("password","*****");
            map.put("sort","1");
            map.put("dId","1234");

            Map<String,String> map2=new HashMap<>();
            map2.put("userName","刘劳务");
            map2.put("password","×××××");
            map2.put("sort","12");
            map2.put("dId","1345");

            uList.add(map);
            uList.add(map2);
            ctx.put("uList", uList);


            router.route().handler(CookieHandler.create());

            // and now delegate to the engine to render it.
            engine.render(ctx, "templates/index.html", res -> {
                if (res.succeeded()) {
                    ctx.response().end(res.result());
                } else {
                    ctx.fail(res.cause());
                }
            });
        });


        // start a HTTP web server on port 8080
        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    }


    public static void main(String[] args){
        Vertx vertx=Vertx.vertx();
        vertx.deployVerticle("com.zb.UserController",stringAsyncResult -> {
            if(stringAsyncResult.failed()){
                stringAsyncResult.cause().printStackTrace();
                System.out.println("deploy fail");
            }else{
                System.out.println("deploy success");
            }
        });
    }

}
