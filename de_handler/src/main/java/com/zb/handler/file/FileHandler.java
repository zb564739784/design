package com.zb.handler.file;

import com.zb.service.file.FileService;
import com.zb.service.file.Impl.FileServiceImpl;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by zhangbo on 17-10-11.
 */
public class FileHandler {

    private static Logger logger = LogManager.getLogger(FileHandler.class);


    private static FileService fileService = new FileServiceImpl();


    private Vertx vertx;

    public FileHandler(Vertx vertx) {
        this.vertx=vertx;
    }

    /**
     * 下载文件
     * @param routingContext
     */
    public void DownFile(RoutingContext routingContext){
        fileService.DownFile(routingContext,vertx);
    }


    /**
     * 上传文件
     * @param routingContext
     */
    public void upload(RoutingContext routingContext){
        fileService.upload(routingContext,vertx);
    }
}
