package org.practice.learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {

    public Integer calcSum(String filePath) throws IOException {
        LineCallback<Integer> sumCallback = new LineCallback<>() {
            @Override
            public Integer doSomethingWithLine(String line, Integer value) {
                return value += Integer.valueOf(line);
            }
        };

        return this.lineReadTemplate(filePath, sumCallback, 0);
    }

    public Integer calcMultiply(String filePath) throws IOException {
        LineCallback<Integer> multiplyCallback = new LineCallback<>() {
            @Override
            public Integer doSomethingWithLine(String line, Integer value) {
                return value *= Integer.valueOf(line);
            }
        };

        return this.lineReadTemplate(filePath, multiplyCallback, 1);
    }

    public String concatenate(String filepath) throws IOException {
        LineCallback<String> concatenateCallback = new LineCallback<>() {
            @Override
            public String doSomethingWithLine(String line, String value) {
                return value += line;
            }
        };

        return this.lineReadTemplate(filepath, concatenateCallback, "");
    }

    private <T> T lineReadTemplate(String filepath, LineCallback<T> callback, T initVal) throws IOException {
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filepath));
            T res = initVal;
            String line = null;

            while((line = br.readLine()) != null) {
                res = callback.doSomethingWithLine(line, res);
            }
            return res;
        } catch (IOException e) {
            throw e;
        } finally {
            if(br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
