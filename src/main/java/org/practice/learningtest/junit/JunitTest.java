package org.practice.learningtest.junit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(locations = "/junit.xml")
@SpringBootTest
public class JunitTest {
    public static Set<JunitTest> testObjects = new HashSet<>();
    public static ApplicationContext contextObject = null;

    @Autowired
    ApplicationContext context;

    @Test
    public void test1() {
        assertThat(this).isNotIn(testObjects);
        testObjects.add(this);

        assertThat(contextObject == null || contextObject == context)
                .isTrue();
        contextObject = context;
    }

    @Test
    public void test2() {
        assertThat(this).isNotIn(testObjects);
        testObjects.add(this);

        assertThat(contextObject == null || contextObject == context)
                .isTrue();
        contextObject = context;
    }

    @Test
    public void test3() {
        assertThat(this).isNotIn(testObjects);
        testObjects.add(this);

        assertThat(contextObject == null || contextObject == context)
                .isTrue();
        contextObject = context;
    }
}
