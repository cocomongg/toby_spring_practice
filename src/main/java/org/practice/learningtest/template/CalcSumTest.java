package org.practice.learningtest.template;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class CalcSumTest {
    private Calculator calculator;
    private String numFilePath;

    @BeforeEach
    public void setUp() {
        calculator = new Calculator();
        numFilePath = getClass().getResource("/numbers.txt").getPath();
    }

    @Test
    public void sumOfNumbers() throws IOException {
        Integer sum = calculator.calcSum(numFilePath);
        assertThat(sum).isEqualTo(10);
    }

    @Test
    public void multiplyOfNumbers() throws IOException {
        Integer multiply = calculator.calcMultiply(numFilePath);
        assertThat(multiply).isEqualTo(24);
    }

    @Test
    public void concatenateStrings() throws IOException {
        String concatenate = calculator.concatenate(numFilePath);
        assertThat(concatenate).isEqualTo("1234");
    }
}
