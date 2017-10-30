package com.zb.service.file.Impl;

import com.zb.service.file.FileService;
import io.vertx.core.Vertx;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.FileSystem;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.streams.Pump;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by zhangbo on 17-10-11.
 */
public class FileServiceImpl implements FileService {

    private static Logger logger = LogManager.getLogger(FileServiceImpl.class);

    /**
     * 下载文件
     *
     * @param routingContext
     */
    @Override
    public void DownFile(RoutingContext routingContext, Vertx vertx) {
        //网页查看(类似A标签)
//            routingContext.request().response().sendFile("/home/zhangbo/testdate/201708223/1502429761850JlbWoS.png");
        vertx.fileSystem().open("/home/zhangbo/testdate/65af0f3d-e17d-402d-ba22-dcc8e88cc372", new OpenOptions(), readEvent -> {
            if (readEvent.failed()) {
                routingContext.response().setStatusCode(500).end();
                return;
            }

            AsyncFile asyncFile = readEvent.result();

            routingContext.response().setChunked(true).putHeader("Content-type", "application/octet-stream")
                    .putHeader("Content-Disposition", " attachment; filename=123.txt");//分块编码

            Pump pump = Pump.pump(asyncFile, routingContext.response());//pump防止文件过大内存溢出

            pump.start();

            asyncFile.endHandler(aVoid -> {
                asyncFile.close();
                pump.stop();
                routingContext.response().end();
            });
        });
    }


    /**
     * 上传文件
     *
     * @param routingContext
     * @param vertx
     */
    @Override
    public void upload(RoutingContext routingContext, Vertx vertx) {
        routingContext.response().putHeader("Content-Type", "text/plain");
        routingContext.response().setChunked(true);
        for (FileUpload f : routingContext.fileUploads()) {
            logger.info("f");
            logger.info("\n");
            logger.info("Filename: " + f.fileName());
            logger.info("\n");
            logger.info("Size: " + f.size());
            logger.info("\n");
            logger.info("uploadedFileName: " + f.uploadedFileName());
            logger.info("\n");
            logger.info("charSet: " + f.charSet());
            logger.info("\n");
            logger.info("name: " + f.name());
            logger.info("\n");
            logger.info("contentType: " + f.contentType());
            logger.info("\n");
            logger.info("contentTransferEncoding: " + f.contentTransferEncoding());
        }
        routingContext.response().end();
    }


    /**
     * 创建上传目录
     *
     * @param fileSystem
     */
    private void makeUploadDir(FileSystem fileSystem) {
        if (!fileSystem.existsBlocking("/home/zhangbo/testdate")) {
            fileSystem.mkdirsBlocking("/home/zhangbo/testdate");
        }
    }

}
