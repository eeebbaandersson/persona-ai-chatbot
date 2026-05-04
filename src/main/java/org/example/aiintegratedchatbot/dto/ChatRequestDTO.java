package org.example.aiintegratedchatbot.dto;

public record ChatRequestDTO(
        String personality,
        String message,
        String sessionId
) {
}
