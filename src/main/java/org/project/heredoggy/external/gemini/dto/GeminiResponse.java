package org.project.heredoggy.external.gemini.dto;

import lombok.Data;

import java.util.List;

@Data
public class GeminiResponse {
    private List<Candidate> candidates;

    public String getFirstText() {
        if (candidates != null && !candidates.isEmpty()) {
            return candidates.get(0).getContent().getParts().get(0).getText();
        }
        return null;
    }

    @Data
    public static class Candidate {
        private Content content;
        private String finishReason;
        private List<SafetyRating> safetyRatings;
    }

    @Data
    public static class Content {
        private List<Part> parts;
    }

    @Data
    public static class Part {
        private String text;
    }

    @Data
    public static class SafetyRating {
        private String category;
        private String probability;
    }
}
