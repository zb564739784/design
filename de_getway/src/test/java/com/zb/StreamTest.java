package com.zb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by zhangbo on 17-7-24.
 */
public class StreamTest {


    public static void main(String[] args) {
//        List<Map<String, Object>> list = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("id", i);
//            map.put("name", "李四" + i);
//            map.put("room_id", "1");
//            map.put("room_name", "中国");
//            list.add(map);
//        }
//        for (int i = 0; i < 10; i++) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("id", i);
//            map.put("name", "张三" + i);
//            map.put("room_id", "2");
//            map.put("room_name", "中国");
//            list.add(map);
//        }

        Map<String,Object> map1 = new HashMap<>();
        map1.put("room_id", "1");
        map1.put("room_name", "中国");
        map1.put("name", "语文简体");
        map1.put("time", "2017-01-01 12:20:10");
        Map<String,Object> map2 = new HashMap<>();
        map2.put("room_id", "1");
        map2.put("room_name", "中国");
        map2.put("name", "语文繁体");
        map2.put("time", "2017-12-12 12:20:10");
        List<Map<String,Object>> list = new ArrayList<>();
        list.add(map1);
        list.add(map2);

        Map<String,Object> map=new HashMap<>();
        List<Map<String,Object>> rsList=new ArrayList<>();
        Object obj=list.parallelStream().collect(Collectors.groupingBy(e->e.remove("room_id")));
        System.out.println("==============");

    }
}
