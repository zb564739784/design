package com.zb.socket;

import javax.net.SocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.Socket;
import java.security.KeyStore;
import java.security.SecureRandom;

/**
 * Created by Administrator on 2017/10/15.
 */
public class SocketClient {
    public static void main(String[] args) {
        try {
            //TODO 加载证书
            String keyName = "D:/IDEAWorkspaces/design/de_tcpClient/src/main/resources/jks/client.jks";
            char[] keyStorePwd = "123456".toCharArray();
            char[] keyPwd = "123456".toCharArray();
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

            // 装载当前目录下的key store. 可用jdk中的keytool工具生成keystore
            InputStream in = null;
            keyStore.load(in=new FileInputStream(keyName), keyPwd);
            in.close();

            // 初始化key manager factory
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory
                    .getDefaultAlgorithm());
            kmf.init(keyStore, keyPwd);


            SSLContext context = SSLContext.getInstance("SSL");
            context.init(kmf.getKeyManagers(),
                    new TrustManager[] { new MyX509TrustManager() },
                    new SecureRandom());



            //1.获取SSLSocketFactory对象
            SocketFactory factory =  context.getSocketFactory();
            //2.从工厂类中获取sslsocket并提供服务器ip和端口参数
            Socket sslsocket = factory.createSocket("127.0.0.1", 1234);
            System.out.println("connetion status isconnect:"+sslsocket.isConnected()+",isclose:"+sslsocket.isClosed());
            //一直读取控制台
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
            while (true){
                //包体
                byte [] content = br.readLine().getBytes();
                //包头,固定4个字节,包含包体长度信息
                BufferedOutputStream bis = new BufferedOutputStream(sslsocket.getOutputStream());
                bis.flush();
                bis.write(content);
                bis.flush();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
