package org.project.heredoggy.external.gemini;

import org.project.heredoggy.external.gemini.dto.GeminiMessage;
import org.project.heredoggy.external.gemini.dto.GeminiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.List;
import java.util.Map;

@Component
public class GeminiClient {
    @Value("${gemini.api-key}")
    private String apiKey;

    @Value("${gemini.model-name}")
    private String modelName;

    @Value("${gemini.api-url}")
    private String apiUrl;

    private final WebClient webClient = WebClient.create();


    public String sendMessages(List<GeminiMessage> messages) {
        String url = String.format("%s/%s:generateContent?key=%s", apiUrl, modelName, apiKey);

        Map<String, Object> requestBody = Map.of("contents", messages);

        GeminiResponse response = webClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(GeminiResponse.class)
                .block();

        return response != null ? response.getFirstText() : null;
    }
}

