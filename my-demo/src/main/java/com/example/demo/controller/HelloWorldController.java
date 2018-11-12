package com.example.demo.controller;

import com.example.demo.config.GateWayProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @Autowired
    GateWayProperties gateWayProperties;

    @Value("${helloworld}")
    String hello;

    @RequestMapping("/hello")
    public String say() {
        return hello;
    }

    @RequestMapping("/show")
    public String show() {
        return gateWayProperties.getNginxHome();
    }
}
