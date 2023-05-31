package com.project.danim_be;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
@EnableEncryptableProperties
public class DanimBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DanimBeApplication.class, args);
    }

}
