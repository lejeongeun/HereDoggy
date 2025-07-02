package org.project.heredoggy.external.gemini.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class GeminiMessage {
    private String role;
    private List<Map<String,String>> parts;

    public GeminiMessage(String role, String text) {
        this.role = role;
        this.parts = List.of(Map.of("text", text));
    }
}
