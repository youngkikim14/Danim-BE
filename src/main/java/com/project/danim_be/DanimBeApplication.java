package com.project.danim_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableScheduling
@EnableWebMvc
@EnableJpaAuditing
@SpringBootApplication
@EnableCaching
public class DanimBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DanimBeApplication.class, args);
    }

}
