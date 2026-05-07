package org.example.aiintegratedchatbot.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.aiintegratedchatbot.dto.ChatRequestDTO;
import org.example.aiintegratedchatbot.dto.ChatResponseDTO;
import org.example.aiintegratedchatbot.service.ChatService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
@Slf4j
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/chat")
    public ChatResponseDTO chat(@RequestBody ChatRequestDTO request) {
        log.info("Chat request received. personality={}, sessionId={}, messageLength={}",
                request.personality(), request.sessionId(),
                request.message() == null ? 0 : request.message().length());
        log.debug("Chat request body: {}", request.message());
       return chatService.handleChat(request);
    }
}
