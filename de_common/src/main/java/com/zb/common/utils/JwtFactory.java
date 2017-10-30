package com.zb.common.utils;

import io.vertx.ext.auth.jwt.JWTAuth;

/**
 * Created by zhangbo on 17-7-10.
 */
public class JwtFactory {

    public static JWTAuth jwtAuth;

    public JwtFactory(JWTAuth jwtAuth) {
        this.jwtAuth=jwtAuth;
    }
}
