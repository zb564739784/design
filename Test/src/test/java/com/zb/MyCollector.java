package com.zb;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class MyCollector implements Collector<Map<String, Object>
        , List<Map<String, Object>>, List<Map<String, Object>>> {

    @Override
    public Supplier<List<Map<String, Object>>> supplier() {
        return () -> new ArrayList<>();
    }

    @Override
    public BiConsumer<List<Map<String, Object>>, Map<String, Object>> accumulator() {
        return (List<Map<String, Object>> acc, Map<String, Object> con) -> {
            Map<String, Object> map = acc.stream().filter(e -> e.get("room_id").equals(con.get("room_id"))).findFirst().orElse(null);
            if (Objects.nonNull(map)) {
                con.remove("room_id");
                con.remove("room_name");
                con.remove("room_no");
                ((List) map.get("data")).add(con);
            } else {
                con.put("data", new ArrayList<HashMap<String, Object>>() {{
                    add(new HashMap<String, Object>() {{
                        put("name", con.get("name"));
                        put("time", con.get("time"));
                        con.remove("name");
                        con.remove("time");
                    }});
                }});
                acc.add(con);
            }
        };
    }


    @Override
    public BinaryOperator<List<Map<String, Object>>> combiner() {
        return null;
    }

    @Override
    public Function<List<Map<String, Object>>, List<Map<String, Object>>> finisher() {
        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH));
    }
}
