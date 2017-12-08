package com.zb.verticle;

import com.zb.handler.PorcessHandler;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.SelfSignedCertificate;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.ResponseContentTypeHandler;
import io.vertx.ext.web.handler.ResponseTimeHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.impl.BodyHandlerImpl;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;
import io.vertx.ext.web.handler.sockjs.SockJSSocket;
import io.vertx.ext.web.impl.FileUploadImpl;
import sun.security.provider.certpath.OCSPResponse;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2017/10/15.
 */
public class HttpServerVerticle extends AbstractVerticle {

    public static final ConcurrentHashMap<SockJSSocket, String> pool = new ConcurrentHashMap<>();

    private String uploadsDir = "D:/搜狗高速下载";

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        Router router = Router.router(vertx);

        router.route("/*").handler(ResponseContentTypeHandler.create());//配置返回类型

        router.post("/ceshi").produces("application/json").handler(routingContext -> {
            routingContext.response().end("{\"msg\":\"ok\"}");
        });//测试

        router.get("/test").handler(routingContext -> {
            PorcessHandler.netSocketList.get(0).write("{\"msg\":\"hahahaha\"}");
            routingContext.response().end("{\"msg\":\"ok\"}");
        });//测试

        router.route("/static/*").handler(StaticHandler.create().setWebRoot("d:/test")
                .setCachingEnabled(false).setDirectoryListing(false));

        router.get("/hello").handler(routingContext -> {
            JsonArray jsonArray = new JsonArray();
            pool.forEach((k, v) -> {
                jsonArray.add(v);
            });
            routingContext.response().end(new JsonObject().put("msg", "ok").put("datalist", jsonArray).toString());
        });//测试

        String serverPath = TcpVerticle.class.getResource("/jks/server.jks").getPath();
        String clientPath = TcpVerticle.class.getResource("/jks/client.jks").getPath();
        vertx.createHttpServer(
                new HttpServerOptions().setSsl(true).setKeyStoreOptions(new JksOptions().setPath(serverPath)
                        .setPassword("123456")).setTrustStoreOptions(new JksOptions().setPath(clientPath).setPassword("123456")))
                .requestHandler(router::accept).websocketHandler(rs -> {
            rs.handler(buffer -> {
                System.out.println(new String(buffer.getBytes()));
            });
        }).listen(config().getInteger("http-port", 8090),
                config().getString("host-name", "0.0.0.0"));


        SockJSHandlerOptions options = new SockJSHandlerOptions().setHeartbeatInterval(2000);

        SockJSHandler sockJSHandler = SockJSHandler.create(vertx, options);

        sockJSHandler.socketHandler(sockJSSocket -> {
            System.out.println("======have you connection===");
            // Just echo the data back
            sockJSSocket.handler(buffer -> {
                HttpServerVerticle.process(buffer, sockJSSocket);
            });
            //end
            sockJSSocket.endHandler(socket -> {
                System.out.println("=====end====" + sockJSSocket);
                pool.remove(sockJSSocket);
            });
            //exception
            sockJSSocket.exceptionHandler(socket -> {
                System.out.println("=====end====" + sockJSSocket);
                pool.remove(sockJSSocket);
            });
        });

        router.route("/myapp/*").handler(sockJSHandler);

        startFuture.complete();
    }


    /**
     * webSocket连接处理
     *
     * @param buffer
     */
    private static void process(Buffer buffer, SockJSSocket sockJSSocket) {
        //accepting only JSON messages
        JsonObject jsonObject = null;
        try {
            jsonObject = new JsonObject(new String(buffer.getBytes()));
        } catch (Exception e) {
            System.out.println("Invalid JSON");
        }

        String conn = "";
        switch (jsonObject.getString("type")) {
            case "login":
                System.out.println("User logged" + jsonObject.getString("name"));
                //if anyone is logged in with this username then refuse
                if (Objects.nonNull(sockJSSocket)) {
                    pool.put(sockJSSocket, jsonObject.getString("name"));
                    speakToAllExceptMe(jsonObject.getString("name"), new JsonObject().put("type", "login").put("success", true).toString());
                }
                break;
            case "offer":
                //for ex. UserA wants to call UserB
                System.out.println("User params:" + jsonObject.toString());
                System.out.println("User logged" + jsonObject.getString("name"));
                //if UserB exists then send him offer details
                conn = jsonObject.getString("name");
                if (conn != null) {
                    //setting that UserA connected with UserB
                    if (pool.size() > 0) {
                        speakToAllExceptMe(jsonObject.getString("connectedUser"), new JsonObject().put("type", "offer").put("offer", jsonObject.getValue("offer"))
                                .put("name", pool.get(sockJSSocket)).toString());
                    }
                }
                break;
            case "answer":
                System.out.println("User logged" + jsonObject.getString("name"));
                //for ex. UserB answers UserA
                conn = jsonObject.getString("name");
                if (conn != null) {
                    //setting that UserA connected with UserB
                    if (pool.size() > 0) {
                        speakToAllExceptMe(jsonObject.getString("connectedUser"), new JsonObject().put("type", "answer").put("answer", jsonObject.getValue("answer")).toString());
                    }
                }
                break;
            case "candidate":
                System.out.println("User logged" + jsonObject.getString("name"));
                conn = jsonObject.getString("name");
                if (conn != null) {
                    if (pool.size() > 0) {
                        speakToAllExceptMe(jsonObject.getString("name"), new JsonObject().put("type", "candidate").put("candidate", jsonObject.getValue("candidate")).toString());
                    }
                }
                break;
            case "leave":
                System.out.println("User logged" + jsonObject.getString("name"));
                conn = jsonObject.getString("name");
                if (conn != null) {
                    if (pool.size() > 0) {
                        speakToAllExceptMe(jsonObject.getString("name"), new JsonObject().put("type", "leave").toString());
                    }
                }
                break;
            default:
                System.out.println("User logged" + jsonObject.getString("name"));
                conn = jsonObject.getString("name");
                if (conn != null) {
                    if (pool.size() > 0) {
                        speakToAllExceptMe(jsonObject.getString("name"), new JsonObject().put("type", "error").put("message", "Command not found").toString());
                    }
                }
                break;
        }

    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        stopFuture.complete();
    }


    /**
     * send message
     *
     * @param name
     * @param msg
     */
    private static void speakToAllExceptMe(String name, String msg) {
        pool.forEach((k, v) -> {
            if (v.equals(name)) {
                System.out.println("send " + name + " Message:" + msg);
                k.write(msg);
            }
        });
    }

}
