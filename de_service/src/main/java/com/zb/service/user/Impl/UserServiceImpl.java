package com.zb.service.user.Impl;

import com.zb.common.constant.RedisConf;
import com.zb.common.general.ErrorType;
import com.zb.common.general.Result;
import com.zb.common.utils.GUID;
import com.zb.common.utils.JwtFactory;
import com.zb.common.verticle.BaseClientVerticle;
import com.zb.dao.user.UserDao;
import com.zb.service.user.UserService;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTOptions;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.templ.ThymeleafTemplateEngine;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Created by Administrator on 2017/4/14.
 */
public class UserServiceImpl implements UserService {

    private static Logger logger = LogManager.getLogger(UserServiceImpl.class);

    private UserDao userDao = new UserDao();

    /**
     * 登录
     *
     * @param username
     * @param pwd
     * @param routingContext
     * @param engine
     */
    @Override
    public void login(String username, String pwd, RoutingContext routingContext, ThymeleafTemplateEngine engine) {
        userDao.login(username, GUID.MD5(pwd), asyncResult -> {
            logger.info("/userServiceImpl/login");
            if (asyncResult.failed()) {
                routingContext.fail(500);
            } else {
                logger.info("/userDao/login/result==" + asyncResult.result());
                Result<Object> result = new Result<>();
                JsonObject resultJson = JsonObject.mapFrom(asyncResult.result());
                //TODO 登录密码验证
                if (Objects.nonNull(asyncResult.result())) {//是否成功登录
                    if (GUID.MD5(pwd + resultJson.getString("salt").toString()).equals((resultJson.getString("password").toString()))) {//登陆成功
                        //添加用戶token
                        Long time = System.currentTimeMillis();
                        String jwtStr = JwtFactory.jwtAuth.generateToken(new JsonObject().put("username", username),
                                new JWTOptions());//jwt加密
                        String[] jwts = StringUtils.split(jwtStr, ".");

                        BaseClientVerticle.writerRedisClient.set(username,
                                jwts[1], res -> {
                                    if (res.failed()) routingContext.fail(500);
                                });//保存token

                        BaseClientVerticle.writerRedisClient.expire(username,
                                Long.parseLong(BaseClientVerticle.properties.get("token_expire_time").toString()), res -> {
                                    if (res.failed()) routingContext.fail(500);
                                });//key过期时间

                        result.setData(new JsonObject().put("token", jwts[1]).put("nickname", resultJson.getString("nickname")));
                        kickUser(username, routingContext, jwts[1]);//踢掉上一个用户
                    } else {
                        result.setErrorMessage(ErrorType.RESULT_LOGIN_FIAL.getKey(), ErrorType.RESULT_LOGIN_FIAL.getValue());
                        BaseClientVerticle.writerRedisClient.hincrby(RedisConf.LOGIN_COUNT, username, 1L
                                , rs -> {
                                    if (rs.failed()) routingContext.fail(rs.cause());
                                });
                    }
                } else {
                    result.setErrorMessage(ErrorType.RESULT_LOGIN_FIAL.getKey(), ErrorType.RESULT_LOGIN_FIAL.getValue());
                }
                routingContext.response().setStatusCode(200).end(JsonObject.mapFrom(result).toString());
            }
        });
    }


    /**
     * 登出
     *
     * @param token
     * @param routingContext
     */
    @Override
    public void logout(String token, RoutingContext routingContext) {
        String username = null;
        try {
            username = new JsonObject(new String(Base64.decodeBase64(token))).getString("username");

            Result<String> result = new Result<String>();
            if (StringUtils.isNotBlank(username)) {
                String finalUsername = username;
                BaseClientVerticle.readRedisClient.get(username, ars -> {
                    if (ars.failed()) {
                        routingContext.fail(500);
                    } else {
                        if (Objects.nonNull(ars.result()) && ars.result().toString().equals(token)) {
                            BaseClientVerticle.writerRedisClient.del(finalUsername, res -> {
                                if (res.failed())
                                    routingContext.fail(500);
                                else
                                    routingContext.response().end(JsonObject.mapFrom(result).toString());
                            });
                        }
                    }
                });

            } else {
                result.setErrorMessage(ErrorType.RESULT_LOG_OUT_FIAL.getKey(), ErrorType.RESULT_LOG_OUT_FIAL.getValue());
                routingContext.response().end(JsonObject.mapFrom(result).toString());
            }
        } catch (Exception e) {
            routingContext.fail(801);
        }
    }

    /**
     * 踢掉上个登录用户
     *
     * @param username
     */
    public void kickUser(String username, RoutingContext routingContext, String token) {
        BaseClientVerticle.readRedisClient.get(username, res -> {
            if (res.failed()) {
                routingContext.fail(500);
            } else {
                if (!res.result().equals(token))
                    BaseClientVerticle.writerRedisClient.set(username, token, rd -> {
                        if (rd.failed()) routingContext.fail(500);
                    });
            }
        });

    }


    @Override
    public void test(RoutingContext routingContext) {
        userDao.test(e -> {
            if (e.failed()) {
                routingContext.fail(500);
            } else {
                Result<Object> result = new Result<>();
                result.setData(e.result());
                routingContext.response().putHeader("Content-Type", "application/json;charset=UTF-8").end(JsonObject.mapFrom(result).toString());
            }
        });
    }
}
