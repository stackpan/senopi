package com.ivanzkyanto.senopi;

import com.ivanzkyanto.senopi.service.StorageService;
import org.apache.tika.Tika;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SenopiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SenopiApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(StorageService storageService) {
        return args -> {
            storageService.init();
        };
    }

    @Bean
    Tika tika() {
        return new Tika();
    }

}
