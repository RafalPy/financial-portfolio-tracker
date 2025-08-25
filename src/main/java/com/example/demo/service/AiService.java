package com.example.demo.service;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.cdimascio.dotenv.Dotenv;


@Service
public class AiService {

    private final WebClient webClient;

    public AiService() {
        // Load .env
        Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("OPENAI_API_KEY");

        if (apiKey == null || apiKey.isEmpty()) {
            throw new RuntimeException(".env key OPENAI_API_KEY is missing");
        }

        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public Mono<String> generateText(String prompt) {
        String requestBody = String.format("""
        {
          "model": "gpt-4o-mini",
          "messages":  [
                         { "role": "system", "content": "You are an over-the-top mix of Jordan Belfort from 'Wolf of Wall Street' and Donald Trump. Speak in a bombastic, persuasive, confident tone. Use big promises, exaggerations, and lots of charisma." },
                         { "role": "user", "content": "%s" }
          ],
          "max_tokens": 150
        }
        """, prompt.replace("\"", "\\\""));

        return webClient.post()
                .uri("/chat/completions")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(json -> {
                    try {
                        return json.get("choices").get(0)
                                .get("message")
                                .get("content")
                                .asText()
                                .trim();
                    } catch (Exception e) {
                        return "Error parsing AI response";
                    }
                });
    }

}






