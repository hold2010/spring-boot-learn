package com.example.demo.lb;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestServiceTest {

    @Test
    public void reload() {

        TestService test = new TestService();
        test.reload("hello");
    }
}