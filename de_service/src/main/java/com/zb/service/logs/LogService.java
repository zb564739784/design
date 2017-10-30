package com.zb.service.logs;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

/**
 * Created by zhangbo on 17-9-7.
 */
public interface LogService {
    void selectLogPage(RoutingContext routingContext,JsonObject resultJson);
}
