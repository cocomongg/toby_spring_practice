package org.practice.learningtest.spring.factorybean;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration(locations = "/FactoryBeanTest-context.xml")
public class FactoryBeanTest {
    @Autowired
    private ApplicationContext context;

    @Test
    public void getMessageFromFactoryBean() {
        Object messageBean = context.getBean("message");

        assertThat(messageBean).isInstanceOf(Message.class);
        assertThat(((Message)messageBean).getText()).isEqualTo("Factory Bean");
    }

    @Test
    public void getFactoryBean() throws Exception {
        Object factoryBean = context.getBean("&message");
        assertThat(factoryBean).isInstanceOf(MessageFactoryBean.class);
    }
}
