package org.example.aiintegratedchatbot.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.example.aiintegratedchatbot.dto.ChatCompletionRequest;
import org.example.aiintegratedchatbot.dto.ChatCompletionResponse;
import org.example.aiintegratedchatbot.exception.AIServiceException;
import org.example.aiintegratedchatbot.exception.RetryableHttpException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
public class AIClientService {

    private final RestClient restClient;

    public AIClientService(RestClient restClient) {
        this.restClient = restClient;
    }


    @CircuitBreaker(name = "aiCircuitBreaker", fallbackMethod = "aiFallback")
    @Retry(name = "aiRetry")
    public ChatCompletionResponse getCompletion(ChatCompletionRequest request) {
        log.info("Skickar anrop till AI-modell: {}", request.model());

            return restClient.post()
                    .uri("/chat/completions")
                    .body(request)
                    .retrieve()
                    .onStatus(status -> status.value() == 429 || status.value() == 503, (req, resp) -> {
                        throw new RetryableHttpException("AI service is busy or unavailable, retrying...");
                    })
                    .body(ChatCompletionResponse.class);



    }

    public ChatCompletionResponse aiFallback(ChatCompletionRequest request, Exception e) {
            log.error("AI service failed. Model: {}, Reason {}", request.model(), e.getMessage());

            String message = "AI service is currently unavailable after multiple attempts.";

            if (e.getMessage() != null && e.getMessage().contains("Connection refused")) {
                message = "Could not connect to the AI service. Make sure LM Studio is using port 1234.";
            }
            throw  new AIServiceException(message, e);
    }
}
