package org.example.aiintegratedchatbot;


import com.github.tomakehurst.wiremock.stubbing.Scenario;
import org.example.aiintegratedchatbot.dto.ChatCompletionRequest;
import org.example.aiintegratedchatbot.dto.ChatCompletionResponse;
import org.example.aiintegratedchatbot.service.AIClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.wiremock.spring.EnableWireMock;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

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

    
    // testCBLogic
}
