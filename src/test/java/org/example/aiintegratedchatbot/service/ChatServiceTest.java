package org.example.aiintegratedchatbot.service;

import org.example.aiintegratedchatbot.dto.ChatCompletionResponse;
import org.example.aiintegratedchatbot.dto.ChatRequestDTO;
import org.example.aiintegratedchatbot.dto.ChatResponseDTO;
import org.example.aiintegratedchatbot.model.ChatMessage;
import org.example.aiintegratedchatbot.model.ChatPersonality;
import org.example.aiintegratedchatbot.model.Choice;
import org.example.aiintegratedchatbot.repository.ChatHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private ChatHistoryRepository chatHistoryRepository;
    @Mock
    private AIClientService aiClientService;

    @InjectMocks
    private ChatService chatService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(chatService, "modelName", "test-model");
    }

    @Test
    void shouldInitializeNewSessionWithSystemPrompt() {
        String sessionId = "test-session";
        ChatRequestDTO request = new ChatRequestDTO(ChatPersonality.DEFAULT,"Hello!",sessionId);

        when(chatHistoryRepository.getMessages(sessionId)).thenReturn(new ArrayList<>());

        ChatMessage assistantMessage = new ChatMessage ("assistant","Hi there!");
        Choice mockChoice = new Choice(assistantMessage);
        ChatCompletionResponse mockResponse = new ChatCompletionResponse(List.of(mockChoice));

        when(aiClientService.getCompletion(any())).thenReturn(mockResponse);

        ChatResponseDTO response = chatService.handleChat(request);

        verify(chatHistoryRepository).addMessage(eq(sessionId), argThat(m -> m.role().equals("system")));
        verify(chatHistoryRepository).addMessage(eq(sessionId), argThat(m -> m.content().equals("Hello!")));
        verify(chatHistoryRepository).addMessage(eq(sessionId), argThat(m -> m.content().equals("Hi there!")));

        verify(aiClientService).getCompletion(argThat(req ->
                req.messages().stream().anyMatch(m -> m.content().equals("Hello!"))
        ));

        assertThat(response.response()).isEqualTo("Hi there!");
    }

    @Test
    void shouldNotAddSystemPromptWhenHistoryExists() {
        String sessionId = "existing-session";
        ChatRequestDTO request = new ChatRequestDTO(ChatPersonality.DEFAULT,"My second message",sessionId);

        List<ChatMessage> existingHistory = new ArrayList<>();
        existingHistory.add(new ChatMessage("system", ChatPersonality.DEFAULT.getSystemPrompt()));
        existingHistory.add(new ChatMessage("user", "My first message"));

        when(chatHistoryRepository.getMessages(sessionId)).thenReturn(existingHistory);

        ChatMessage assistantMessage = new ChatMessage ("assistant","I remember you!");
        ChatCompletionResponse mockResponse = new ChatCompletionResponse(List.of(new Choice(assistantMessage)));

        when(aiClientService.getCompletion(any())).thenReturn(mockResponse);

        chatService.handleChat(request);

        verify(chatHistoryRepository, never())
                .addMessage(eq(sessionId), argThat(m -> m.role().equals("system")));

        verify(chatHistoryRepository).addMessage(eq(sessionId), argThat(m -> m.content().equals("My second message")));

    }
}
