package org.practice.learningtest.template;

public interface LineCallback<T> {
    T doSomethingWithLine(String line, T value);
}
