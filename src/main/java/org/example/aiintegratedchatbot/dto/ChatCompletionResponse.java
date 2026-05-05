package org.example.aiintegratedchatbot.dto;

import org.example.aiintegratedchatbot.model.Choice;

import java.util.List;

public record ChatCompletionResponse(
      List<Choice> choices

) {
}
