package com.zb.dao.logs;

import com.zb.common.constant.ElasticConf;
import com.zb.common.verticle.BaseClientVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;


/**
 * Created by zhangbo on 17-9-7.
 */
public class LogDao {

    /**
     * 查询日志列表
     *
     *
     * {
     "sort":[{
     "time":"desc"
     }
     ],
     "query":{
     "bool":{
     "must":[{
     "range": {
     "time": {
     "gte":  "2017-09-15 00:00:00",
     "lte":  "2017-09-23 23:59:59"
     }
     }
     },
     {
     "multi_match": {
     "query":"V",
     "fields":   [ "content", "classLine","exception" ],
     "fuzziness": "AUTO"
     }
     }
     ]
     }
     },
     "_source":["time","threads","classLine","content","exception"]
     }
     */
    public void selectLogPage(JsonObject jsonObject,Handler<AsyncResult> handler) {
        System.out.println(jsonObject.toString());
        BaseClientVerticle.webClient.post(ElasticConf.LOG_INDEX).sendJsonObject(jsonObject,ar->{
            if (ar.failed()) {
                ar.cause().printStackTrace();
            }else {
                handler.handle(Future.succeededFuture(ar.result().bodyAsJsonObject()));
            }
        });
    }
}
