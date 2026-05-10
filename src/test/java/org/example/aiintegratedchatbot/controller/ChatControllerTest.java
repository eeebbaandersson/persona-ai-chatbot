package org.example.aiintegratedchatbot.controller;

import org.example.aiintegratedchatbot.dto.ChatRequestDTO;
import org.example.aiintegratedchatbot.dto.ChatResponseDTO;
import org.example.aiintegratedchatbot.service.ChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ChatService chatService;


    // Success
    @Test
    void handleChat_shouldReturnOk_WhenRequestIsValid() throws Exception {

        ChatResponseDTO mockResponse = new ChatResponseDTO("Hi there!", "123");

        when(chatService.handleChat(any(ChatRequestDTO.class))).thenReturn(mockResponse);

        String jsonRequest = "{\"message\": \"Hello\", \"personality\": \"DEFAULT\", \"sessionId\": \"123\"}";

        mockMvc.perform(post("/api/v1/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("Hi there!"))
                .andExpect(jsonPath("$.sessionId").value("123"));
    }


    // Service Error
    @Test
    void handleChat_shouldReturnServerError_WhenServerFails() throws Exception {

        when(chatService.handleChat(any(ChatRequestDTO.class)))
                .thenThrow(new RuntimeException("AI Service Failure"));

        String jsonRequest = "{\"message\": \"Hello\", \"personality\": \"DEFAULT\", \"sessionId\": \"123\"}";

        mockMvc.perform(post("/api/v1/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isInternalServerError());
    }

    // Validation Error
    @Test
    void handleChat_shouldReturnBadRequest_WhenMessageIsEmpty() throws Exception {

        String invalidJson = "{\"message\": \"\", \"personality\": \"DEFAULT\", \"sessionId\": \"123\"}";

        mockMvc.perform(post("/api/v1/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(chatService);

    }
}
