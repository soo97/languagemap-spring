package kr.co.mapspring.global.exception.ai;

public class AssistantMessageRequiredException extends RuntimeException {

    public AssistantMessageRequiredException() {
        super("AI 메시지는 비어 있을 수 없습니다.");
    }
}