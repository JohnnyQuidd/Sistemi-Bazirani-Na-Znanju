package com.example.siemcenter;

import org.kie.internal.utils.KieHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SiemCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(SiemCenterApplication.class, args);
    }

    @Bean
    public KieHelper kieHelper() {
        return new KieHelper();
    }
}
