package ru.practicum.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.exp.stat.client.StatsClient;

/**
 * Класс конфигурации StatsClient'a
 */
@Configuration
public class StatsClientConfig {
    @Value("${stats-module-url}")
    private String serverUrl;

    @Bean
    public StatsClient statsClient() {
        return new StatsClient(serverUrl);
    }
}