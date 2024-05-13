package org.practice.learningtest.proxy;

import org.junit.jupiter.api.Test;

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
}
