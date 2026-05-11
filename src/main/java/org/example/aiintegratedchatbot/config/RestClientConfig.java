package org.example.aiintegratedchatbot.config;

import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient(RestClient.Builder builder,
                                 @Value("${ai.api.base-url}") String baseUrl,
                                 @Value("${ai.api.key}") String apiKey) {

        var httpClient = HttpClients.custom()
                .disableAutomaticRetries()
                .build();

        var requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

        return builder
                .requestFactory(requestFactory)
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();

    }
}
