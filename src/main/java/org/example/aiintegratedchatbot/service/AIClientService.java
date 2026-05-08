package org.example.aiintegratedchatbot.service;

import lombok.extern.slf4j.Slf4j;
import org.example.aiintegratedchatbot.dto.ChatCompletionRequest;
import org.example.aiintegratedchatbot.dto.ChatCompletionResponse;
import org.example.aiintegratedchatbot.exception.AIServiceException;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
public class AIClientService {

    private final RestClient restClient;

    public AIClientService(RestClient restClient) {
        this.restClient = restClient;
    }

    public ChatCompletionResponse getCompletion(ChatCompletionRequest request) {
        log.info("Skickar anrop till AI-modell: {}", request.model());

        try {
            return restClient.post()
                    .uri("/chat/completions")
                    .body(request)
                    .retrieve()
                    .body(ChatCompletionResponse.class);
        } catch (Exception e) {
            log.error("Kunde inte kommunicera med AI-tjänsten: {}", e.getMessage());
            throw new AIServiceException("Could not connect to the AI-service. Make sure LM Studio is using port 1234.");

        }
    }
}
