package com.zb.handler;

import com.zb.common.constant.HttpAttrType;
import com.zb.common.general.ErrorType;
import com.zb.common.general.Result;
import com.zb.common.utils.EscapeUtils;
import com.zb.common.verticle.BaseClientVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.*;
import io.vertx.ext.web.templ.ThymeleafTemplateEngine;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by zhangbo on 17-4-30.
 */
public class BaseHandler {

    private Set<String> allowHeaders = new HashSet<>();//cors跨域header

    private Set<HttpMethod> allowMethods = new HashSet<>();//请求方法

    private static Logger logger = LogManager.getLogger(BaseHandler.class);

    public static ThymeleafTemplateEngine engine;//配置模板


    /**
     * 跨域资源Cors配置  /[^loginCode]  可以正则匹配
     *
     * @param router
     */
    public void enableCorsSupport(Router router) {
        allowMethods.add(HttpMethod.GET);
        allowMethods.add(HttpMethod.POST);
        allowMethods.add(HttpMethod.DELETE);
        allowMethods.add(HttpMethod.PUT);

        allowHeaders.add("Content-Type");
        allowHeaders.add("token");
        router.route("/*").handler(CorsHandler.create("*")//设置请求域Origin的范围*(reqeust不能带上cookies)代表所有
                .allowedHeaders(allowHeaders)
                .allowedMethods(allowMethods));

    }


    /**
     * 配置静态资源路径
     *
     * @param router
     */
    public void staticResource(Router router) {
        router.route("/static/*").handler(StaticHandler.create().setWebRoot("static")
                .setCachingEnabled(false).setDirectoryListing(false));
    }


    /**
     * 配置Body和upload
     */
    public void bodyOrUpload(Router router) {
        router.route().handler(BodyHandler.create().setUploadsDirectory("/home/zhangbo/testdate"));
    }


    /**
     * 记录HTTP请求的日志
     *
     * @param router
     */
    public void requestlog(Router router) {
        router.route("/*").handler(LoggerHandler.create(LoggerFormat.DEFAULT));//请求接口日志
    }


    /**
     * JWT config
     *
     * @param vertx
     * @return
     */
    public JWTAuth JWTConf(Vertx vertx) {
        String path = BaseHandler.class.getResource("/jceks/keystore.jceks").getPath();
        // Create a JWT Auth Provider
        JWTAuth jwt = JWTAuth.create(vertx, new JsonObject()
                .put("keyStore", new JsonObject()
                        .put("type", "jceks")
                        .put("path", path)
                        .put("password", "secret")));
        return jwt;
    }


    /**
     * 全局异常和超时处理
     */
    public void ExceptionAndTimeout(Router router) {
        /** 全局异常处理*/
        router.route("/*").failureHandler(failureRoutingContext -> {
            int statusCode = failureRoutingContext.statusCode();//获取错误状态码

            HttpServerRequest request = failureRoutingContext.request();
            HttpServerResponse response = failureRoutingContext.response();
            Result<Object> result = new Result<>();
            if (statusCode >= 0) {//判断是否出现异常
                switch (statusCode) {//全局异常处理
                    case 404:
                        result.setErrorMessage(ErrorType.RESULT_RESOURCES_NOT_FIND.getKey(), ErrorType.RESULT_RESOURCES_NOT_FIND.getValue());
                        response.end(JsonObject.mapFrom(result).toString());
                        break;
                    case 500:
                        result.setErrorMessage(ErrorType.RESULT_SERVER_FIAL.getKey(), ErrorType.RESULT_SERVER_FIAL.getValue());
                        response.end(JsonObject.mapFrom(result).toString());
                        break;
                    case 801://恶意登录，不是正确的token
                        result.setErrorMessage(ErrorType.RESULT_LOGIN_EVIL.getKey(), ErrorType.RESULT_LOGIN_EVIL.getValue());
                        response.end(JsonObject.mapFrom(result).toString());
                        break;
                    case 806://参数格式不正确
                        result.setErrorMessage(ErrorType.RESULT_LOGIN_ILLEGAL.getKey(), ErrorType.RESULT_LOGIN_ILLEGAL.getValue());
                        response.end(JsonObject.mapFrom(result).toString());
                        break;
                    case 509://處理超時
                        result.setErrorMessage(ErrorType.RESULT_SERVER_TIME_OUT.getKey(), ErrorType.RESULT_SERVER_TIME_OUT.getValue());
                        response.end(JsonObject.mapFrom(result).toString());
                        break;
                    case 401://必需的参数没有传或参数类型不对null值
                        result.setErrorMessage(ErrorType.RESULT_PARAMS_FAIL.getKey(), ErrorType.RESULT_PARAMS_FAIL.getValue());
                        response.end(JsonObject.mapFrom(result).toString());
                        break;
                    default:
                        result.setErrorMessage(ErrorType.RESULT_UNKONWN.getKey(), ErrorType.RESULT_UNKONWN.getValue());
                        response.end(JsonObject.mapFrom(result).toString());
                        break;
                }

                //写入错误日志
                if (Objects.nonNull(failureRoutingContext.failure()))
                    logger.error("Exception = {},{}",request.uri(), failureRoutingContext.failure());//打印异常消息
                else
                    logger.error("Exception = {},{}",request.uri(), JsonObject.mapFrom(result).toString());//打印异常消息
            } else {//打印异常日志
                result.setErrorMessage(ErrorType.RESULT_SERVER_FIAL.getKey(), ErrorType.RESULT_SERVER_FIAL.getValue());
                response.end(JsonObject.mapFrom(result).toString());
                logger.error("Exception = {},{}",request.uri(), failureRoutingContext.failure());//打印异常消息
            }
        }).handler(TimeoutHandler.create(2000, 509));//超时处理,返回错误码;

    }


    /**
     * 配置模板
     */
    public void template() {
        engine = ThymeleafTemplateEngine.create();
    }


    /**
     * 配置content-type返回类型
     */
    public void produces(Router router) {
        router.route("/*").handler(ResponseContentTypeHandler.create());
    }


//    /**
//     * 登录拦截
//     *
//     * @param router
//     */
//    public void loginIntercept(Router router) {
//        router.route("/[^loginCode][^logout][^login][^test]*").handler(routingContext -> {
//            logger.info("/*===loginIntercept" + routingContext.getBodyAsString());
//            isLogin(routingContext);
//        }).consumes(HttpAttrType.CONTENT_TYPE_JSON.getValue());
//    }


    /**
     * 全局的编码转义
     *
     * @param router
     */
    public void globalIntercept(Router router) {
        router.route("/*").handler(routingContext -> {
            logger.info("/*===globalIntercept = {}", routingContext.getBodyAsString());
            String params = EscapeUtils.escapeHtml(routingContext.getBodyAsString());//对特殊字符转义
            if (this.paramsIsJsonObject(routingContext, params)) {
                if (!BaseClientVerticle.excludePathList.contains(routingContext.request().uri()))
                    isLogin(routingContext);
                else
                    routingContext.next();
            } else {
                routingContext.fail(806);//非法参数
            }
        }).consumes(HttpAttrType.CONTENT_TYPE_JSON.getValue());

    }

    /**
     * 参数是否是jsonObject
     */
    public boolean paramsIsJsonObject(RoutingContext routingContext, String params) {
        boolean flag = true;
        //验证参数
        if (Objects.nonNull(params) && params.length() > 0) {
            try {
                JsonObject jsonObject = new JsonObject(params);
            } catch (Exception e) {
                flag = false;
            }
            routingContext.put("params", params);
        }
        return flag;
    }


    /**
     * 用戶是否登錄
     *
     * @return
     */
    public void isLogin(RoutingContext routingContext) {
        Result<Object> result = new Result<>();
        String token = routingContext.request().getHeader("token");
        validationToken(token, res -> {
            if (res.failed())
                routingContext.fail(res.cause());
            else if (Boolean.parseBoolean(res.result().toString())) {
                routingContext.next();
            } else {
                result.setErrorMessage(ErrorType.RESULT_LOGIN_NO.getKey(), ErrorType.RESULT_LOGIN_NO.getValue());
                routingContext.response().end(JsonObject.mapFrom(result).toString());
            }
        }, routingContext, true);
    }


    /**
     * 验证token的有效性
     *
     * @param token
     */
    public void validationToken(String token, Handler<AsyncResult> handler, RoutingContext routingContext, boolean refresh) {
        if (StringUtils.isNotBlank(token)) {
            String username = null;
            try {
                username = new JsonObject(new String(Base64.decodeBase64(token))).getString("username");
                if (null != username) {
                    String finalUsername = username;
                    BaseClientVerticle.readRedisClient.get(username, res -> {
                        if (res.failed()) {
                            handler.handle(Future.failedFuture(res.cause()));
                        } else if (Objects.nonNull(res.result()) && res.result().toString().equals(token)) {
                            handler.handle(Future.succeededFuture(true));
                            if (refresh) //刷新token时间
                                BaseClientVerticle.writerRedisClient.expire(finalUsername,
                                        Long.parseLong(BaseClientVerticle.properties.get("token_expire_time").toString()), rs -> {
                                            if (rs.failed()) routingContext.fail(500);
                                        });//key过期时间
                        } else {
                            handler.handle(Future.succeededFuture(false));
                        }
                    });
                }
            } catch (Exception e) {
                handler.handle(Future.succeededFuture(false));
            }
        } else {
            handler.handle(Future.succeededFuture(false));
        }
    }


}




























