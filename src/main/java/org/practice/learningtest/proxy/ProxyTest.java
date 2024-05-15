package org.practice.learningtest.proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import java.lang.reflect.Proxy;

import static org.assertj.core.api.Assertions.assertThat;

public class ProxyTest {
    @Test
    public void simpleProxy() {
        Hello proxiedHello = new HelloUppercase(new HelloTarget());

        assertThat(proxiedHello.sayHello("toby")).isEqualTo("HELLO TOBY");
        assertThat(proxiedHello.sayHi("toby")).isEqualTo("HI TOBY");
        assertThat(proxiedHello.sayThankYou("toby")).isEqualTo("THANK YOU TOBY");
    }

    @Test
    public void dynamicProxy() {
        Hello proxiedHello = (Hello)Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{Hello.class},
                new UppercaseHandler(new HelloTarget())
        );

        assertThat(proxiedHello.sayHello("toby")).isEqualTo("HELLO TOBY");
        assertThat(proxiedHello.sayHi("toby")).isEqualTo("HI TOBY");
        assertThat(proxiedHello.sayThankYou("toby")).isEqualTo("THANK YOU TOBY");
    }

    @Test
    public void proxyFactoryBean() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());
        pfBean.addAdvice(new UppercaseAdvice());

        Hello proxiedHello = (Hello) pfBean.getObject();

        assertThat(proxiedHello.sayHello("toby")).isEqualTo("HELLO TOBY");
        assertThat(proxiedHello.sayHi("toby")).isEqualTo("HI TOBY");
        assertThat(proxiedHello.sayThankYou("toby")).isEqualTo("THANK YOU TOBY");
    }

    public static class UppercaseAdvice implements MethodInterceptor {

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            String ret = (String)invocation.proceed();
            return ret.toUpperCase();
        }
    }

    @Test
    public void pointcutAdvisor() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*");

        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));

        Hello proxiedHello = (Hello) pfBean.getObject();

        assertThat(proxiedHello.sayHello("toby")).isEqualTo("HELLO TOBY");
        assertThat(proxiedHello.sayHi("toby")).isEqualTo("HI TOBY");
        assertThat(proxiedHello.sayThankYou("toby")).isEqualTo("Thank You toby");
    }
}
