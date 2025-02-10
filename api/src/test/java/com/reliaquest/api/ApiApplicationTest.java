package com.reliaquest.api;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
public class ApiApplicationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void contextLoads() {
        assertNotNull(applicationContext);
    }

    @Test
    public void restTemplateBeanExists() {
        RestTemplate restTemplate = applicationContext.getBean(RestTemplate.class);
        assertNotNull(restTemplate);
    }
}
