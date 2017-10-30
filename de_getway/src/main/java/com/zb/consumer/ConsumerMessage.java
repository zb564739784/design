package com.zb.consumer;

import com.zb.handler.LogMessage.LogMessageHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by zhangbo on 17-9-29.
 */
public class ConsumerMessage {

    private static Logger logger = LogManager.getLogger(ConsumerMessage.class);

    /*×
     *消息消费
     */
    public void consumer(){
        LogMessageHandler logMessageHandler=new LogMessageHandler();
        logMessageHandler.receiving();
    }
}
