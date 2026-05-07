package org.example.aiintegratedchatbot.service;

import org.example.aiintegratedchatbot.model.ChatPersonality;
import org.example.aiintegratedchatbot.dto.ChatCompletionRequest;
import org.example.aiintegratedchatbot.dto.ChatCompletionResponse;
import org.example.aiintegratedchatbot.dto.ChatRequestDTO;
import org.example.aiintegratedchatbot.dto.ChatResponseDTO;
import org.example.aiintegratedchatbot.model.ChatMessage;
import org.example.aiintegratedchatbot.repository.ChatHistoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    private final ChatHistoryRepository chatHistoryRepository;
    private final AIClientService aiClientService;

    @Value("${ai.api.model-name}")
    private String modelName;

    public ChatService(ChatHistoryRepository chatHistoryRepository, AIClientService aiClientService) {
        this.chatHistoryRepository = chatHistoryRepository;
        this.aiClientService = aiClientService;
    }

    public ChatResponseDTO handleChat(ChatRequestDTO request){
        List<ChatMessage> history = chatHistoryRepository.getMessages(request.sessionId());

        if (history.isEmpty()){
            ChatPersonality personality = request.personality() != null
                    ? request.personality()
                    : ChatPersonality.DEFAULT;
            history.add(new ChatMessage("system", personality.getSystemPrompt()));
        }

        history.add(new ChatMessage("user", request.message()));

        ChatCompletionRequest aiRequest = new ChatCompletionRequest(modelName, history);

        ChatCompletionResponse aiResponse = aiClientService.getCompletion(aiRequest);

        if (aiResponse == null || aiResponse.choices() == null || aiResponse.choices().isEmpty()) {
            return new ChatResponseDTO(
                    "Kunde inte ansluta till AI-tjänsten. Kontrollera att LM Studio körs på port 1234.",
                    request.sessionId()
            );
        }

        String aiMessage = aiResponse.choices().get(0).message().content();

        history.add(new ChatMessage("assistant", aiMessage));

        return new ChatResponseDTO(aiMessage, request.sessionId());
    }
}
