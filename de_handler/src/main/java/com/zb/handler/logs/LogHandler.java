package com.zb.handler.logs;

import com.zb.common.verify.VerifyParamsUtil;
import com.zb.handler.BaseHandler;
import com.zb.handler.user.UserHandler;
import com.zb.service.logs.Impl.LogServiceImpl;
import com.zb.service.logs.LogService;
import com.zb.service.user.Impl.UserServiceImpl;
import com.zb.service.user.UserService;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by zhangbo on 17-6-19.
 */
public class LogHandler extends BaseHandler {
    private static Logger logger = LogManager.getLogger(UserHandler.class);


    private static LogService logService = new LogServiceImpl();

    /**
     * 查看日志
     *
     * @param routingContext
     */
    public void selectLog(RoutingContext routingContext) {
        //TODO p,当前页  pageSize:每页记录数  nPages: 页码数
        VerifyParamsUtil.verifyParams(routingContext, new JsonObject().put("pageSize", Integer.class.getName()).put("p", Integer.class.getName())
                .put("nPages", Integer.class.getName()).put("type",String.class.getName()), ar -> {
            if (ar.failed())
                routingContext.fail(401);
            else
                logService.selectLogPage(routingContext,JsonObject.mapFrom(ar.result()));
        });
    }

}
