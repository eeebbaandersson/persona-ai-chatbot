package org.example.aiintegratedchatbot.dto;

import jakarta.validation.constraints.NotBlank;
import org.example.aiintegratedchatbot.model.ChatPersonality;

public record ChatRequestDTO(
        ChatPersonality personality,

        @NotBlank(message = "Message cannot be empty")
        String message,

        @NotBlank(message = "Session ID is required")
        String sessionId
) {
}
