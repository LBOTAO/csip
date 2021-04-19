package com.fsmer.csip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CsipapiApplication /*extends SpringBootServletInitializer*/ {

    /**
     * tomcat启动
     */
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        return builder.sources(CsipapiApplication.class);
//    }
    public static void main(String[] args) {
        SpringApplication.run(CsipapiApplication.class, args);
    }

}
