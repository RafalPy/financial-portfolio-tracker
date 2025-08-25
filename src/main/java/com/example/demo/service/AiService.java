package com.example.demo.service;

import com.example.demo.dto.PortfolioHolding;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class AiService {

    private final WebClient webClient;
    private final TransactionService transactionService;


    public AiService(TransactionService transactionService) {

        this.transactionService = transactionService;
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

    public Mono<String> generateLandingPageText() {

        // 1. Get holdings
        List<PortfolioHolding> myPortfolio = transactionService.getPortfolioHoldings();

        // 2. Make them human-readable
        String portfolioString = myPortfolio.stream()
                .map(holding -> holding.getAssetSymbol() + ": " + holding.getTotalQuantity() + " shares @ $" + holding.getAveragePrice())
                .collect(Collectors.joining("\n"));

        // 3. Build the request body as a Map
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o-mini",
                "messages", List.of(
                        Map.of(
                                "role", "system",
                                "content", "You are a certified financial advisor with 10+ years of experience in wealth management." +
                                        " Provide clear, actionable advice. Use simple language but include examples, pros and cons, and risk considerations. I have given you my portfolio holdings data. Try to speak on each possible asset. If possible get news from MarketWatch.com top stories headlines" +
                                        "and link it to our portfolio holdings. You need to use current affairs based on the news if possible." +
                                        "not more than 5 sentences "
//                                        "about my portfolio selection or any other financial advice in general."
                        ),
                        Map.of(
                                "role", "user",
                                "content", "Here’s my portfolio:\n" + portfolioString
                        )
                ),
                "max_tokens", 150
        );

        // 4. Call OpenAI
        return webClient.post()
                .uri("/chat/completions")
                .bodyValue(requestBody) //
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







