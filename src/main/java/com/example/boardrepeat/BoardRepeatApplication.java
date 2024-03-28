package com.example.boardrepeat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class BoardRepeatApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoardRepeatApplication.class, args);
    }

}
