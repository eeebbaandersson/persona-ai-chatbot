package org.example.aiintegratedchatbot.service;


import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import org.example.aiintegratedchatbot.dto.ChatCompletionRequest;
import org.example.aiintegratedchatbot.dto.ChatCompletionResponse;
import org.example.aiintegratedchatbot.exception.AIServiceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.wiremock.spring.EnableWireMock;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(properties = {"ai.api.base-url=${wiremock.server.baseUrl}/v1"})
@EnableWireMock
 class AIClientServiceTest {

    @Autowired
    private AIClientService aiClientService;

    @Test
    void testRetryAndSuccessLogic() {

        String fullPath = "/v1/chat/completions";

        stubFor(post(urlEqualTo(fullPath))
                .inScenario("Retry Scenario")
                .whenScenarioStateIs(Scenario.STARTED)
                .willReturn(aResponse().withStatus(503))
                .willSetStateTo("First Failure"));

        stubFor(post(urlEqualTo(fullPath))
                .inScenario("Retry Scenario")
                .whenScenarioStateIs("First Failure")
                .willReturn(aResponse().withStatus(503))
                .willSetStateTo("Second Failure"));

        stubFor(post(urlEqualTo(fullPath))
                .inScenario("Retry Scenario")
                .whenScenarioStateIs("Second Failure")
                .willReturn(okJson("{\"choices\": [{\"message\": {\"content\": \"Success!\"}}]}")));

        ChatCompletionRequest request = new ChatCompletionRequest("test-model", List.of());
        ChatCompletionResponse response = aiClientService.getCompletion(request);

        assertThat(response.choices().getFirst().message().content()).isEqualTo("Success!");

        // WireMock verification --> ensure it hit the server 3 times
        verify(3, postRequestedFor(urlEqualTo(fullPath)));
    }


    @Test
    void testCircuitBreakerLogic() throws InterruptedException {
        String fullPath = "/v1/chat/completions";
        ChatCompletionRequest request = new ChatCompletionRequest("test-model", List.of());

        stubFor(post(urlEqualTo(fullPath))
                .willReturn(aResponse().withStatus(503)
                        .withHeader("Content-Type", "application/json")
                .withBody("Fail!")));

        for (int i = 0; i <10; i++) {
            try {
                aiClientService.getCompletion(request);
            } catch (Exception e) {
                // Ignore exceptions in the loop, handled by CircuitBreaker
            }
        }

        // Verify OPEN state
        resetAllRequests();

        assertThatThrownBy(() -> aiClientService.getCompletion(request))
                .isInstanceOf(AIServiceException.class)
                .hasMessageContaining("AI service is currently unavailable after multiple attempts.");

        verify(0, postRequestedFor(urlEqualTo(fullPath)));

        stubFor(post(urlEqualTo(fullPath))
                .willReturn(okJson("{\"choices\": [{\"message\": {\"content\": \"AI-service is back again!\"}}]}")));

        Thread.sleep(10001);

        ChatCompletionResponse recoveryResponse = aiClientService.getCompletion(request);

        assertThat(recoveryResponse.choices().getFirst().message().content()).isEqualTo("AI-service is back again!");

        verify(1, postRequestedFor(urlEqualTo(fullPath)));

    }

    @Test
    void getCompletion_ShouldReturnAIServiceException_On429TooManyRequests() {
        String fullPath = "/v1/chat/completions";

        stubFor(post(urlEqualTo(fullPath))
                .willReturn(aResponse().withStatus(429)));

        ChatCompletionRequest request = new ChatCompletionRequest("test-model", List.of());

        assertThatThrownBy(() -> aiClientService.getCompletion(request))
                .isInstanceOf(AIServiceException.class);

        verify(3, postRequestedFor(urlEqualTo(fullPath)));
    }

    @Test
    void getCompletion_ShouldReturnAIServiceException_OnMalformedJson() {
        String fullPath = "/v1/chat/completions";

        stubFor(post(urlEqualTo(fullPath))
                .willReturn(ok().withBody("{ \"choices\": [ { \"message\": \"invalid\" ] }")));

        ChatCompletionRequest request = new ChatCompletionRequest("test-model", List.of());

        assertThatThrownBy(() -> aiClientService.getCompletion(request))
                .isInstanceOf(AIServiceException.class)
                .hasMessageContaining("AI service is currently unavailable");
    }

    @Test
    void getCompletion_ShouldReturnAIServiceException_OnNetworkFault() {
        String fullPath = "/v1/chat/completions";

        stubFor(post(urlEqualTo(fullPath))
                .willReturn(aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER)));

        ChatCompletionRequest request = new ChatCompletionRequest("test-model", List.of());

        assertThatThrownBy(() -> aiClientService.getCompletion(request))
        .isInstanceOf(AIServiceException.class);

        verify(3, postRequestedFor(urlEqualTo(fullPath)));
    }
}
