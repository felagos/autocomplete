package com.autocomplete;

import com.autocomplete.service.TrieInitializerService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableAsync
@EnableScheduling
public class AutocompleteApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutocompleteApplication.class, args);
    }

    @Bean
    @Order(2)
    public ApplicationRunner trieInitializer(TrieInitializerService trieInitializerService) {
        return args -> trieInitializerService.initialize();
    }
}
