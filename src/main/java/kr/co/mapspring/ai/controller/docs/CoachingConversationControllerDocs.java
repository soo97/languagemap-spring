package kr.co.mapspring.ai.controller.docs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import io.swagger.v3.oas.annotations.Parameter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mapspring.ai.dto.CoachingConversationDto;
import kr.co.mapspring.global.dto.ApiResponseDTO;

@Tag(name = "AI Coaching Conversation", description = "AI 코칭 대화 흐름 API")
public interface CoachingConversationControllerDocs {

    @Operation(
            summary = "AI 코칭 스크립트 준비",
            description = "사용자가 선택한 코칭 옵션을 기준으로 FastAPI에서 스크립트를 생성하고 DB에 저장합니다."
    )
    ResponseEntity<ApiResponseDTO<CoachingConversationDto.ResponsePrepareScript>> prepareScript(
            @RequestBody CoachingConversationDto.RequestPrepareScript request
    );

    @Operation(
            summary = "AI 코칭 대화 시작",
            description = "저장된 첫 번째 스크립트 턴의 AI 문장을 TTS로 변환하고 메시지로 저장합니다."
    )
    ResponseEntity<ApiResponseDTO<CoachingConversationDto.ResponseStartConversation>> startConversation(
    		@PathVariable("coachingSessionId") Long coachingSessionId
    );

    @Operation(
            summary = "사용자 음성 처리",
            description = "사용자 음성을 FastAPI 발음 평가로 전송하고, USER 메시지/발음 결과/다음 AI 메시지를 저장합니다."
    )
    ResponseEntity<ApiResponseDTO<CoachingConversationDto.ResponseConversationTurn>> processUserSpeech(
            @PathVariable("coachingSessionId") Long coachingSessionId,

            @Parameter(description = "사용자 음성 파일", required = true)
            @RequestPart("audioFile") MultipartFile audioFile
    );

    @Operation(
            summary = "AI 코칭 최종 평가",
            description = "전체 대화와 발음 평가 결과를 기반으로 최종 피드백을 생성하고 추천 유튜브 콘텐츠를 저장합니다."
    )
    ResponseEntity<ApiResponseDTO<CoachingConversationDto.ResponseFinishConversation>> finishConversation(
    		@PathVariable("coachingSessionId") Long coachingSessionId
    );
}