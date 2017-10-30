package com.zb.api;

import com.zb.common.constant.ApiConf;
import com.zb.common.constant.HttpAttrType;
import com.zb.common.general.Result;
import com.zb.common.utils.JwtFactory;
import com.zb.handler.BaseHandler;
import com.zb.handler.IndexHandler;
import com.zb.handler.LoginHandler;
import com.zb.handler.file.FileHandler;
import com.zb.handler.logs.LogHandler;
import com.zb.handler.user.UserHandler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.apache.kafka.common.security.auth.Login;
import org.eclipse.core.internal.resources.File;
import org.eclipse.core.internal.runtime.Log;

/**
 * Created by zhangbo on 17-6-19.
 */
public class ApiPathConf {


    /**
     * 加载api
     * @param vertx
     * @param router
     */
    public static void  uploadApi(Vertx vertx, Router router){
        /**
         * 通用配置
         */
        BaseHandler baseHandler=new BaseHandler();
        baseHandler.bodyOrUpload(router);
        baseHandler.enableCorsSupport(router);
        baseHandler.ExceptionAndTimeout(router);
        baseHandler.staticResource(router);
        baseHandler.requestlog(router);
        baseHandler.globalIntercept(router);
        JWTAuth jwtAuth=baseHandler.JWTConf(vertx);
        baseHandler.template();//配置模板
        baseHandler.produces(router);//配置返回类型
        //jwt配置
        JwtFactory jwtFactory=new JwtFactory(jwtAuth);


        /**
         * 登录处理
         */
        LoginHandler loginHandler = new LoginHandler();
        router.post(ApiConf.USERLOGIN).consumes(HttpAttrType.CONTENT_TYPE_JSON.getValue()).produces(HttpAttrType.CONTENT_TYPE_JSON.getValue()).handler(loginHandler::login);//登录
        router.post(ApiConf.USERLOGOUT).consumes(HttpAttrType.CONTENT_TYPE_JSON.getValue()).handler(loginHandler::logout);//登出
        router.get(ApiConf.LOGINCODE).handler(loginHandler::loginCode);//登錄驗證碼
        router.post(ApiConf.ISREQUIREDCODE).consumes(HttpAttrType.CONTENT_TYPE_JSON.getValue()).handler(loginHandler::isLoginCode);//登錄驗證碼

//        router.get("/test").handler(userHandler::test);//测试
        /**
         * 主頁
         */
        IndexHandler indexHandler=new IndexHandler();
        router.post(ApiConf.INDEX).handler(indexHandler::index);//主頁

        /**
         * 日志
         */
        LogHandler logHandler = new LogHandler();
        router.post(ApiConf.SELECTLOGPAGE).consumes(HttpAttrType.CONTENT_TYPE_JSON.getValue()).handler(logHandler::selectLog).produces(HttpAttrType.CONTENT_TYPE_JSON.getValue());//查询日志列表

        /**
         * 文件
         */
        FileHandler fileHandler = new FileHandler(vertx);
        router.get(ApiConf.DOWNFILE).handler(fileHandler::DownFile);//文件下载
        router.post(ApiConf.UPLOAD).handler(fileHandler::upload);//文件上传
    }
}



















