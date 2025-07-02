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

    public boolean isUnsafe() {
        if (candidates == null || candidates.isEmpty()) return false;

        List<SafetyRating> ratings = candidates.get(0).getSafetyRatings();
        if (ratings == null) return false;

        // 위험 확률이 MEDIUM 이상인 경우 차단
        return ratings.stream().anyMatch(rating ->
                "MEDIUM".equalsIgnoreCase(rating.getProbability()) ||
                        "HIGH".equalsIgnoreCase(rating.getProbability())
        );
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
