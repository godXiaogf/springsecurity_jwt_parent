package com.xiao;

import com.xiao.config.RsaPath;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@MapperScan("com.xiao.mapper")
@EnableConfigurationProperties(RsaPath.class)
public class AuthServerApp {
    public static void main(String[] args) {
        SpringApplication.run(AuthServerApp.class, args);
    }
}
