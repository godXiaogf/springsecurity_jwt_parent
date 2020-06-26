package com.xiao;

import com.xiao.config.RsaPath;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaPath.class)
public class SourceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SourceApplication.class, args);
    }
}
