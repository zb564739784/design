package com.zb.handler;

import io.vertx.core.net.NetSocket;

/**
 * Created by zhangbo on 17-10-12.
 */
public class PorcessHandler {

    /**
     * 服务处理
     */
    public void process(NetSocket netSocket){
        netSocket.write("I connetion server");
        netSocket.handler(buffer -> {
                System.out.println("TCP CLIENT I received some bytes: " + buffer.length());
                System.out.println("TCP CLIENT body: " + new String(buffer.getBytes()));
        });
    }

}
