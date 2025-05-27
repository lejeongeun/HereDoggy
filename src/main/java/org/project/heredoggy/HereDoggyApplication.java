package org.project.heredoggy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HereDoggyApplication {

    public static void main(String[] args) {
        SpringApplication.run(HereDoggyApplication.class, args);
    }
}
