package com.xiaofutest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class SpringRootTestApplication {

    public static void main(String[] args) {
//        SpringApplication.run(SpringRootTestApplication.class, args);
        SpringApplication app  = new SpringApplication(SpringRootTestApplication.class);
        app.setDefaultProperties(Collections
                .singletonMap("server.port", "8088"));
        app.run(args);
    }


}
