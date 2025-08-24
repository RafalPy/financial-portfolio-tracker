package com.example.demo.controller;


import com.example.demo.service.AiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiChatController {

    private final AiService aiChatService;

    public AiChatController(AiService aiChatService) {
        this.aiChatService = aiChatService;
    }

    @PostMapping("/chat")
    public Mono<ResponseEntity<String>> chatWithAI(@RequestBody Map<String, String> payload) {
        String userMessage = payload.get("message");
        if (userMessage == null || userMessage.isBlank()) {
            return Mono.just(ResponseEntity.badRequest().body("Message is empty"));
        }

        return aiChatService.generateText(userMessage)
                .map(text -> ResponseEntity.ok(text))
                .onErrorResume(e -> Mono.just(
                        ResponseEntity.status(500).body("AI service error: " + e.getMessage())
                ));
    }
}

