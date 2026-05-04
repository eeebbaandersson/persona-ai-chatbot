package org.example.aiintegratedchatbot.dto;

import org.example.aiintegratedchatbot.model.ChatMessage;

import java.util.List;

public record ChatCompletionRequest(
        String model,
        List<ChatMessage> messages
) {
}
