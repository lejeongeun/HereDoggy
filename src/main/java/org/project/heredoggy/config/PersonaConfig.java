package org.project.heredoggy.config;

import lombok.Getter;
import org.project.heredoggy.external.gemini.dto.GeminiMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class PersonaConfig {

    @Value("${chatbot.persona-text}")
    private String personaText;

    public GeminiMessage getPersonaMessage() {
        return new GeminiMessage("user", personaText);
    }
}