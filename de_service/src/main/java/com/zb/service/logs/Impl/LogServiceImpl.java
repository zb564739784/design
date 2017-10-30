package com.zb.service.logs.Impl;

import com.zb.common.constant.HttpAttrType;
import com.zb.common.general.Result;
import com.zb.common.page.DataListPage;
import com.zb.dao.logs.LogDao;
import com.zb.service.logs.LogService;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by zhangbo on 17-9-7.
 */
public class LogServiceImpl implements LogService {

    private static Logger logger = LogManager.getLogger(LogServiceImpl.class);

    private LogDao logDao = new LogDao();

    /**
     * 查询日志分页
     */
    @Override
    public void selectLogPage(RoutingContext routingContext, JsonObject resultJson) {
        Integer page = resultJson.getInteger("p");//当前页
        Integer page_size = resultJson.getInteger("pageSize");//每页记录数
        Integer nPages = resultJson.getInteger("nPages");//nPages

        Integer formPage = page_size * (page - 1);//偏移量

        //TODO 搜素条件
        String fileds_content="";
        if(resultJson.getString("type").equals("1"))
            fileds_content="content";
        else
            fileds_content="exception";
        JsonObject paramsJson = new JsonObject();//查询参数
        if (Objects.nonNull(resultJson.getValue("startTime")) && resultJson.getString("startTime").length() > 0
                && Objects.nonNull(resultJson.getValue("startTime")) && resultJson.getString("startTime").length() > 0
                && !Objects.nonNull(resultJson.getValue("content")) && resultJson.getString("content").length() <= 0) {//时间区段查询
            paramsJson.put("query", new JsonObject().put("bool", new JsonObject().put("time", new JsonObject().put("gte", resultJson.getValue("startTIme"))
                    .put("lte", resultJson.getString("endTIme")))));
        } else if (!Objects.nonNull(resultJson.getValue("startTime")) && resultJson.getString("startTime").length() <= 0
                && !Objects.nonNull(resultJson.getValue("startTime")) && resultJson.getString("startTime").length() <= 0
                && Objects.nonNull(resultJson.getValue("content")) && resultJson.getString("content").length() > 0) {//搜素相关内容
            paramsJson.put("query", new JsonObject().put("multi_match", new JsonObject().put("query", resultJson.getValue("content"))
                    .put("fuzziness", "AUTO")).put("fields", new JsonArray().add(fileds_content).add("classLine")));
        } else if (Objects.nonNull(resultJson.getValue("startTime")) && resultJson.getString("startTime").length() > 0
                && Objects.nonNull(resultJson.getValue("startTime")) && resultJson.getString("startTime").length() > 0
                && Objects.nonNull(resultJson.getValue("content")) && resultJson.getString("content").length() > 0) {//内容加时间区间搜素
            paramsJson.put("query", new JsonObject().put("bool", new JsonObject().put("must", new JsonArray().add(new JsonObject().put("range"
                    , new JsonObject().put("time", new JsonObject().put("gte", resultJson.getValue("startTIme")).put("lte", resultJson.getString("startTIme")))))
                    .add(new JsonObject().put("multi_match", new JsonObject().put("query", resultJson.getValue("content")).put("fuzziness", "AUTO").put("fields", new JsonArray()
                            .add("classLine").add(fileds_content)))))));
        } else{//没有查询条件
            paramsJson.put("query", new JsonObject().put("exists", new JsonObject().put("field", fileds_content)));
        }

        logDao.selectLogPage(paramsJson.put("sort", new JsonArray().add(new JsonObject().put("time", "desc")))
                        .put("from", formPage).put("size", page_size).put("_source", new JsonArray().add(fileds_content).add("time").add("threads").add("classLine"))
                , ar -> {
                    if (ar.failed()) {
                        routingContext.fail(500);
                    } else {
                        JsonObject jsonObject = JsonObject.mapFrom(ar.result()).getJsonObject("hits");
                        if (Objects.nonNull(jsonObject)) {
                            Integer total = jsonObject.getInteger("total");
                            if (Objects.nonNull(total) && total > 0) {
                                List data = ((List<Map<String, Object>>) jsonObject.getJsonArray("hits").getList())
                                        .stream().map(e -> JsonObject.mapFrom(e.get("_source")).put("id", e.get("_id"))).collect(Collectors.toList());

                                int page_total = total % page_size == 0 ? total / page_size : total / page_size + 1;//总页数
                                int[] navigatepageNums = DataListPage.calcNavigatepageNums(page_total, nPages, page);//获取页面标签数组
                                routingContext.response().setStatusCode(200).end(JsonObject.mapFrom(
                                        new Result().setData(new JsonObject().put("dataList", data).put("total", total)
                                                .put("navigatepageNums", new JsonArray(Arrays.asList(navigatepageNums))).put("p", page).put("pages", page_total)
                                                .put("pageNum", data.size()))).toString());
                            } else {
                                routingContext.response().setStatusCode(200).end(JsonObject.mapFrom(new Result()).toString());
                            }
                        } else {
                            routingContext.response().setStatusCode(200).end(JsonObject.mapFrom(new Result()).toString());
                        }
                    }
                });
    }
}
