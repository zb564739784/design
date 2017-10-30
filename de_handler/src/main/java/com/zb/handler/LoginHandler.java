package com.zb.handler;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.zb.common.code.BfferImageCss;
import com.zb.common.constant.RedisConf;
import com.zb.common.general.ErrorType;
import com.zb.common.general.Result;
import com.zb.common.verify.VerifyParamsUtil;
import com.zb.common.verticle.BaseClientVerticle;
import com.zb.service.user.Impl.UserServiceImpl;
import com.zb.service.user.UserService;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by zhangbo on 17-9-13.
 */
public class LoginHandler extends BaseHandler{

    private static Logger logger = LogManager.getLogger(LoginHandler.class);


    private static UserService userService = new UserServiceImpl();

    /**
     * 登录验证
     *
     * @param routingContext
     */
    public void login(RoutingContext routingContext) {
        logger.info("/userHandler/login = {}" , routingContext.getBodyAsString());

        VerifyParamsUtil.verifyParams(routingContext, new JsonObject().put("username", String.class.getName())
                .put("pwd", String.class.getName()), res -> {
            if (res.failed()) {
                routingContext.fail(401);
            } else {
                Result<Object> result = new Result<>();
                JsonObject resultJson = JsonObject.mapFrom(res.result());
                //TODO 检验是否有验证码
                if (Objects.nonNull(resultJson.getString("key"))
                        && resultJson.getString("key") instanceof String && resultJson.getString("key").trim().length() > 0) {
                    verifykey(resultJson, routingContext, result);
                } else {
                    BaseClientVerticle.readRedisClient.hget(RedisConf.LOGIN_COUNT, resultJson.getString("username"), rs -> {
                        if (rs.failed()) {
                            routingContext.fail(rs.cause());
                        } else {
                            if (Objects.nonNull(rs.result()) && Integer.parseInt(rs.result().toString()) >= 3) {//判断是否有验证码
                                verifykey(resultJson, routingContext, result);
                            } else {//判断传过来key的问题
                                userService.login(resultJson.getString("username"), resultJson.getString("pwd"), routingContext, engine);
                            }
                        }
                    });
                }
            }
        });

    }

    /**
     * 验证key
     *
     * @param resultJson
     * @param routingContext
     * @param result
     */
    public void verifykey(JsonObject resultJson, RoutingContext routingContext, Result<Object> result) {
        if (Objects.nonNull(resultJson.getString("code")) && resultJson.getString("code") instanceof String
                && resultJson.getString("code").trim().length() > 0 && Objects.nonNull(resultJson.getString("key"))
                && resultJson.getString("key") instanceof String && resultJson.getString("key").trim().length() > 0) {//数据不对
            BaseClientVerticle.readRedisClient.get(resultJson.getString("key"), ars -> {//判断验证码是否正确
                if (ars.failed()) {
                    routingContext.fail(ars.cause());
                } else {
                    if (Objects.nonNull(ars.result()) && ars.result().toString().equals(resultJson.getString("code"))) {
                        userService.login(resultJson.getString("username"), resultJson.getString("pwd"), routingContext, engine);
                    } else {
                        result.setErrorMessage(ErrorType.RESULT_CODE_FAIL.getKey(), ErrorType.RESULT_CODE_FAIL.getValue());
                        routingContext.response().setStatusCode(200).end(JsonObject.mapFrom(result).toString());
                    }
                }
            });
        }else{
            result.setErrorMessage(ErrorType.RESULT_CODE_FAIL.getKey(), ErrorType.RESULT_CODE_FAIL.getValue());
            routingContext.response().setStatusCode(200).end(JsonObject.mapFrom(result).toString());
        }
    }


    /**
     * 退出登录
     *
     * @param routingContext
     */

    public void logout(RoutingContext routingContext) {
        logger.info("/userHandler/logout = {}" , routingContext.getBodyAsString());
        JsonObject jsonObject = null;
        String params = routingContext.getBodyAsString();
        if (StringUtils.isNotBlank(params)) {
            try {
                jsonObject = new JsonObject(params);

                if (Objects.nonNull(jsonObject.getValue("token")) && jsonObject.getValue("token") instanceof String
                        && jsonObject.getString("token").trim().length() > 0) {
                    userService.logout(jsonObject.getString("token"), routingContext);
                } else {
                    Result<String> result = new Result<String>();
                    result.setErrorMessage(ErrorType.RESULT_LOG_OUT_FIAL.getKey(), ErrorType.RESULT_LOG_OUT_FIAL.getValue());
                    routingContext.response().end(JsonObject.mapFrom(result).toString());
                }
            } catch (Exception e) {
                routingContext.fail(806);//非法参数
            }
        } else {
            Result<String> result = new Result<String>();
            result.setErrorMessage(ErrorType.RESULT_LOG_OUT_FIAL.getKey(), ErrorType.RESULT_LOG_OUT_FIAL.getValue());
            routingContext.response().end(JsonObject.mapFrom(result).toString());
        }

    }


    /**
     * 登录驗證碼
     *
     * @param routingContext
     */
    public void loginCode(RoutingContext routingContext) {
        logger.info("/userHandler/loginCode = {}" , routingContext.request().getParam("key"));
        String key = routingContext.request().getParam("key");
        if (!Objects.nonNull(key)) {
            Result<String> result = new Result<>();
            result.setErrorMessage(ErrorType.RESULT_DATA_FAIL.getKey(), ErrorType.RESULT_DATA_FAIL.getValue());
            routingContext.response().end(JsonObject.mapFrom(result).toString());
        }
        byte[] b = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //生成图片
        BufferedImage challenge = BfferImageCss.getInstance().getImageChallengeForID(UUID.randomUUID().toString());
        //加密
        JPEGImageEncoder jpegEncoder = JPEGCodec.createJPEGEncoder(baos);
        try {
            jpegEncoder.encode(challenge);
            b = baos.toByteArray();
            routingContext.response().putHeader("content-Length", String.valueOf(b.length))
                    .putHeader("Cache-Control", "no-store").putHeader("content-Type", "image/jpeg")
                    .putHeader("Pragma", "no-cache").putHeader("Expires", "0").write(Buffer.buffer(b)).end();
            BaseClientVerticle.writerRedisClient.set(key, String.valueOf(BfferImageCss.result), res -> {
                if (res.failed()) routingContext.fail(res.cause());
            });
            BaseClientVerticle.writerRedisClient.expire(key, Long.parseLong(BaseClientVerticle.properties.get("login_code_expire_time").toString())
                    , res -> {
                        if (res.failed()) routingContext.fail(res.cause());
                    });//5分钟有效时间
        } catch (IOException e) {
            routingContext.fail(e);//驗證碼異常
        } finally {
            try {
                if (Objects.nonNull(baos))
                    baos.close();
            } catch (IOException e) {
                routingContext.fail(e);//驗證碼異常
            }
        }
    }


    /**
     * 是否显示验证码
     *
     * @param routingContext
     */
    public void isLoginCode(RoutingContext routingContext) {
        logger.info("/userHandler/isLoginCode = {}" , routingContext.getBodyAsString());
        Result<Object> result = new Result<>();
        VerifyParamsUtil.verifyParams(routingContext, new JsonObject().put("username", String.class.getName()), res -> {
            if (res.failed()) {
                routingContext.fail(401);
            } else {
                JsonObject resultJson = JsonObject.mapFrom(res.result());
                BaseClientVerticle.readRedisClient.hget(RedisConf.LOGIN_COUNT, resultJson.getString("username"), rs -> {
                    if (rs.failed()) {
                        routingContext.fail(rs.cause());
                    } else {
                        String uuid = UUID.randomUUID().toString();
                        if (Objects.nonNull(rs.result()) && Integer.parseInt(rs.result().toString())
                                >= Integer.parseInt(BaseClientVerticle.properties.get("login_count_code").toString())) {
                            result.setData(new JsonObject().put("imgSrc", BaseClientVerticle.properties.get("login_code_url")
                                    .toString().replace("code_url", uuid)));
                        }
                        routingContext.response().end(JsonObject.mapFrom(result).toString());
                    }
                });
            }
        });

    }

    /**
     * 测试
     * @param routingContext
     */
    public void test(RoutingContext routingContext){
        Long startTime=System.currentTimeMillis();
        logger.info("/userHandler/isLoginCode = {}" , routingContext.getBodyAsString());
        userService.test(routingContext);
        System.out.println("\n\n=="+(System.currentTimeMillis()-startTime)+"\n\n===");
    }
}
