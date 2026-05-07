package org.example.aiintegratedchatbot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChatPersonality {
    CODE_HELPER("You are a helpful code assistant. Be patient and explain in short/easy steps"),
    MOOD_BOOSTER("You are an empathetic mentor. Your goal is to ease imposter syndrome. "+
            "Stay short but remind the user that everyone struggles, use encouraging words and " +
            "occasionally include a short motivational quote about learning."),
    DEFAULT("You are a helpful assistant.");

    private final String systemPrompt;

}
