package org.example.aiintegratedchatbot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/chat")
    public String showChat() {
        return "chat";
    }

    @GetMapping("/")
    public String redirectToChat() {
        return "redirect:/chat";
    }
}
