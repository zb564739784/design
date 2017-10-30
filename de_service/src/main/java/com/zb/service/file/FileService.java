package com.zb.service.file;

import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;

/**
 * Created by zhangbo on 17-10-11.
 */
public interface FileService {
    void DownFile(RoutingContext routingContext, Vertx vertx);

    void upload(RoutingContext routingContext, Vertx vertx);
}
