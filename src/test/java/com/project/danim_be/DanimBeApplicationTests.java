package com.project.danim_be;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"jasypt.password=${JASYPT_PASSWORD:testpassword}"})
class DanimBeApplicationTests {

    @Test
    void contextLoads() {
    }

}
