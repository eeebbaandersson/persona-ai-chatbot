package org.example.aiintegratedchatbot.dto;

import org.example.aiintegratedchatbot.ChatPersonality;

public record ChatRequestDTO(
        ChatPersonality personality,
        String message,
        String sessionId
) {
}
