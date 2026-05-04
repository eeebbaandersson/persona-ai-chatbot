package org.example.aiintegratedchatbot.controller;

import org.example.aiintegratedchatbot.dto.ChatRequestDTO;
import org.example.aiintegratedchatbot.dto.ChatResponseDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ChatController {



    @PostMapping("/chat")
    public ChatResponseDTO chat(@RequestBody ChatRequestDTO request) {
        String mockResponse = "Test response message: '"
                +request.message()
                + "' for personality: "
                +request.personality();

        return new ChatResponseDTO(mockResponse, request.sessionId());

    }
}
