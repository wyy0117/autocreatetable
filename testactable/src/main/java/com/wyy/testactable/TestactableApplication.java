package com.wyy.testactable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.wyy.actable.*", "com.wyy.testactable.*"})
public class TestactableApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestactableApplication.class, args);
    }

}
