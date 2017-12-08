package com.zb;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhangbo on 17-9-29.
 */
public class PatterTest {

    Pattern PATTERN_EMAIL = Pattern.compile("([^(\\s]+) *\\((.*)\\)");

    public static void main(String[] args){
        PatterTest patterTest=new PatterTest();
        Matcher matcher = patterTest.PATTERN_EMAIL.matcher("[no-reply@1sju.com(12313)");
        if(!matcher.matches()) {
            System.out.println("========error");
        }else{
            System.out.println("========ok");
            System.out.println("========"+matcher.group(1));
            System.out.println("========"+matcher.group(2));
        }
    }

}
