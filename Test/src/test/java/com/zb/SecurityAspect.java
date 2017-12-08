package com.zb;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by Administrator on 2017/4/17.
 */
@Aspect
public class SecurityAspect {

    @Pointcut("@annotation(com.zb.MyComponent)")
    public void secureAccess() {
        System.out.println("123123123123");
    }

    @Before("secureAccess()")
    public void secure() {
        System.out.println("Checking and authenticating user");
    }
}
