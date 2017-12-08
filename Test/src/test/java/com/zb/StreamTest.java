package com.zb;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.thymeleaf.expression.Maps;

import javax.print.DocFlavor;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by zhangbo on 17-8-14.
 */
public class StreamTest {

    public static void main(String[] args){
//        public static void main(String[] args) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map1 = new HashMap<>();
            map1.put("room_id", 1);
            map1.put("room_name", "中国");
            map1.put("room_no", "TB213123");
            map1.put("name", "语文简体" + i);
            map1.put("time", "2017-01-01 12:20:10");
            list.add(map1);

        }
        for (int i = 10; i < 20; i++) {
            Map<String, Object> map3 = new HashMap<>();
            map3.put("room_id", 2);
            map3.put("room_name", "外国");
            map3.put("room_no", "TB21345");
            map3.put("name", "english" + i);
            map3.put("time", "2017-12-12 12:20:11");
            list.add(map3);
        }

        Long startTime=System.currentTimeMillis();
        List<Map<String,Object>> cc=list.stream().collect(new MyCollector());
        Long endTime=System.currentTimeMillis();
        System.out.println("time = "+(endTime-startTime));
        System.out.println("cc = "+cc);

//        Long startTime = System.currentTimeMillis();
//        List<Map<String, Object>> resultList = new ArrayList<>();
//        list.stream().collect(Collectors.groupingBy(e -> e.remove("room_id"), Collectors.groupingBy(c -> c.remove("room_name"),
//                Collectors.groupingBy(r -> r.remove("room_no")))))
//                .forEach((k, v) -> {
//                    Map<String, Object> map = new HashMap<>();
//                    map.put("room_id", k);
//                    v.forEach((k2, v2) -> {
//                        map.put("room_name", k2);
//                        List<Map<String, Object>> rs = new ArrayList<>();
//                        v2.forEach((k3, v3) -> {
//                            map.put("room_no", k3);
//                            v3.forEach(mm -> {
//                                Map<String, Object> m = new HashMap<>();
//                                m.put("name", mm.get("name"));
//                                m.put("time", mm.get("time"));
//                                rs.add(m);
//                            });
//                        });
//                        map.put("data", rs);
//                        resultList.add(map);
//                    });
//                });
//        Long endTime = System.currentTimeMillis();//纳秒
//        System.out.println("time = " + (endTime - startTime));
//        System.out.println("resultList = " + resultList);


    }


}
