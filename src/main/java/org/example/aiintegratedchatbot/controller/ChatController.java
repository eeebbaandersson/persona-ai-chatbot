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

//    @GetMapping("/test-ai")
//    public ChatResponseDTO testAi() {
//        ChatRequestDTO testRequest = new ChatRequestDTO(
//                "mood-booster",
//                "Tell me something uplifting",
//                "test-session-123"
//        );
//        return chatService.handleChat(testRequest);
//
//    }

    @PostMapping("/chat")
    public ChatResponseDTO chat(@RequestBody ChatRequestDTO request) {
        log.info("Request received! Message: " + request.message());
       return chatService.handleChat(request);
    }
}
