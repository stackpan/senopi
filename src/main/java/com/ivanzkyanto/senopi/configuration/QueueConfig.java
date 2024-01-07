package com.ivanzkyanto.senopi.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfig {

    @Bean
    public Queue exportNotesQueue() {
        return new Queue("export:notes");
    }

}
