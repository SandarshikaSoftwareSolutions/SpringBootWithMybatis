package com.practice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:spring/applicationContext.xml")
//@ActiveProfiles({"dev", "default"})
public class SpringMybatisWithTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringMybatisWithTestApplication.class, args);
    }

}
