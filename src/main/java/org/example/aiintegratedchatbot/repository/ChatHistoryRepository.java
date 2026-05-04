package org.example.aiintegratedchatbot.repository;

import org.example.aiintegratedchatbot.model.ChatMessage;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ChatHistoryRepository {

    private final Map<String, List<ChatMessage>> history = new ConcurrentHashMap<>();

//    public List<ChatMessage> getMessages(String sessionID) {}

//    public void addMessage(String sessionId, ChatMessage chatMessage) {}
}
