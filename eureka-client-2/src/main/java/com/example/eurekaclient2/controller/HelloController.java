package com.example.eurekaclient2.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @RequestMapping(value = "/2", method = RequestMethod.GET)
    public String say() {
        return "hello client1";
    }

}