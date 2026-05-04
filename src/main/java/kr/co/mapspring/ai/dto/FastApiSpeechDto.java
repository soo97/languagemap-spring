package kr.co.mapspring.ai.dto;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class FastApiSpeechDto {

    @Getter
    @NoArgsConstructor
    @Schema(description = "FastAPI TTS 요청 DTO")
    public static class RequestTts {

        private String text;

        @Builder
        public RequestTts(String text) {
            this.text = text;
        }
    }

    @Getter
    @NoArgsConstructor
    @Schema(description = "FastAPI TTS 응답 DTO")
    public static class ResponseTts {
        private String audioUrl;
    }

    @Getter
    @NoArgsConstructor
    @Schema(description = "FastAPI STT 응답 DTO")
    public static class ResponseStt {
        private String recognizedText;
    }

    @Getter
    @NoArgsConstructor
    @Schema(description = "FastAPI 발음 평가 응답 DTO")
    public static class ResponsePronunciationAssessment {

        private String recognizedText;
        private Double accuracyScore;
        private Double fluencyScore;
        private Double completenessScore;
        private Double pronunciationScore;
        private String feedback;
        private List<PronunciationProblemWord> problemWords;
    }

    @Getter
    @NoArgsConstructor
    @Schema(description = "FastAPI 발음 문제 단어 DTO")
    public static class PronunciationProblemWord {
        private String word;
        private Double score;
        private String feedback;
    }

    @Getter
    @NoArgsConstructor
    @Schema(description = "FastAPI 문제 단어 음성 요청 DTO")
    public static class RequestProblemWordAudio {

        private String word;

        @Builder
        public RequestProblemWordAudio(String word) {
            this.word = word;
        }
    }

    @Getter
    @NoArgsConstructor
    @Schema(description = "FastAPI 문제 단어 음성 응답 DTO")
    public static class ResponseProblemWordAudio {
        private String word;
        private String audioUrl;
    }
}