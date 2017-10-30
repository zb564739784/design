package com.zb.handler;

import com.zb.common.general.ErrorType;
import com.zb.common.general.Result;
import com.zb.common.template.TemplateConf;
import com.zb.common.verticle.BaseClientVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

/**
 * Created by zhangbo on 17-6-23.
 */
public class IndexHandler extends BaseHandler {


    private static Logger logger = LogManager.getLogger(IndexHandler.class);

    /**
     * 主頁
     *
     * @param routingContext
     */
    public void index(RoutingContext routingContext) {
        logger.info("/IndexHandler/index = {}" , routingContext.getBodyAsString());
        Result<Object> result = new Result<>();
        routingContext.response().setStatusCode(200).end(JsonObject.mapFrom(result).toString());
    }

}
