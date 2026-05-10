package org.example.aiintegratedchatbot.controller;

import org.example.aiintegratedchatbot.service.AIClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChatController.class)
public class ChatControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private AIClientService aiClientService;


}
