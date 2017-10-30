package com.zb.common.utils;

import javax.crypto.KeyGenerator;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.KeyStore;

/**
 * Created by Administrator on 2017/4/19.
 */
public class JceksUtil {

    public static void toJceks(){
        try{
            KeyStore keyStore = KeyStore.getInstance("JCEKS");
            keyStore.load(null, null);

            KeyGenerator keyGen = KeyGenerator.getInstance("DES");
            keyGen.init(56);;
            Key key = keyGen.generateKey();

            keyStore.setKeyEntry("secret", key, "password".toCharArray(), null);

            keyStore.store(new FileOutputStream("output.jceks"), "password".toCharArray());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args){
        JceksUtil.toJceks();
    }
}
