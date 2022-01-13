package com.osgi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceResource {

    @Autowired
    OSGiLauncher osgi;

    @RequestMapping("/osgi")
    public String hello() {

        osgi.init();


        return "Hello World RESTful with Spring Boot";
    }
}