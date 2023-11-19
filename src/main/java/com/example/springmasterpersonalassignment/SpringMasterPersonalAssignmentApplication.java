package com.example.springmasterpersonalassignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringMasterPersonalAssignmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringMasterPersonalAssignmentApplication.class, args);
    }

}
