package com.aipm.ai_project_management.modules.ai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class AIServiceConfig {
    
    @Value("${app.ai.service.base-url:http://localhost:5000}")
    private String aiServiceBaseUrl;
    
    @Value("${app.ai.service.connect-timeout:5000}")
    private int connectTimeout;
    
    @Value("${app.ai.service.read-timeout:30000}")
    private int readTimeout;
    
    @Bean("aiRestTemplate")
    public RestTemplate aiRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(clientHttpRequestFactory());
        return restTemplate;
    }
    
    private ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(connectTimeout);
        factory.setReadTimeout(readTimeout);
        return factory;
    }
    
    public String getAiServiceBaseUrl() {
        return aiServiceBaseUrl;
    }
}