package com.zb.service.user;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.templ.ThymeleafTemplateEngine;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/14.
 */
public interface UserService {
     void login(String username, String pwd, RoutingContext routingContext, ThymeleafTemplateEngine engine);

     void logout(String token,RoutingContext routingContext);


     void test(RoutingContext routingContext);

}
