package com.zb.handler;

import io.vertx.core.net.NetSocket;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangbo on 17-10-12.
 */
public class PorcessHandler {

    public static List<NetSocket> netSocketList=new ArrayList<>();

    /**
     * 服务处理
     */
    public void process(NetSocket netSocket){
        netSocketList.add(netSocket);
        netSocket.handler(buffer -> {
            System.out.println("TCP SERVER I received remoteAddress: " +netSocket.remoteAddress());
            System.out.println("TCP SERVER I received some bytes: " + buffer.length());
            System.out.println("TCP SERVER body: " + new String(buffer.getBytes()));
            speakToAllExceptMe(netSocket, "["+netSocket.writeHandlerID()+"] : 连接成功");
        });
        //end
        netSocket.endHandler(socket->{
            System.out.println("=====end====");
           netSocketList.remove(socket);
        });
        //close
        netSocket.closeHandler(socket->{
            System.out.println("=====close====");
            netSocketList.remove(socket);
        });
        //exception
        netSocket.exceptionHandler(socket->{
            System.out.println("=====exception====");
            netSocketList.remove(socket);
        });
    }


    private void speakToAllExceptMe(NetSocket me, String msg) {
        for(NetSocket s : netSocketList) {
            if(me.equals(s)) s.write(msg);
        }
    }

}
