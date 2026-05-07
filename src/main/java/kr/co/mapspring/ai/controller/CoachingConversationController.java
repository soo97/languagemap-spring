package kr.co.mapspring.ai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import kr.co.mapspring.ai.controller.docs.CoachingConversationControllerDocs;
import kr.co.mapspring.ai.dto.CoachingConversationDto;
import kr.co.mapspring.ai.service.CoachingConversationService;
import kr.co.mapspring.global.dto.ApiResponseDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/coaching/conversation")
@RequiredArgsConstructor
public class CoachingConversationController implements CoachingConversationControllerDocs {

    private final CoachingConversationService coachingConversationService;

    @Override
    @PostMapping("/script")
    public ResponseEntity<ApiResponseDTO<CoachingConversationDto.ResponsePrepareScript>> prepareScript(
            @RequestBody CoachingConversationDto.RequestPrepareScript request
    ) {
        CoachingConversationDto.ResponsePrepareScript response =
                coachingConversationService.prepareScript(request);

        return ResponseEntity.ok(
                ApiResponseDTO.success("AI 코칭 스크립트 준비 성공", response)
        );
    }

    @Override
    @PostMapping("/{coachingSessionId}/start")
    public ResponseEntity<ApiResponseDTO<CoachingConversationDto.ResponseStartConversation>> startConversation(
            @PathVariable("coachingSessionId") Long coachingSessionId
    ) {
        CoachingConversationDto.ResponseStartConversation response =
                coachingConversationService.startConversation(coachingSessionId);

        return ResponseEntity.ok(
                ApiResponseDTO.success("AI 코칭 대화 시작 성공", response)
        );
    }

    @Override
    @PostMapping(
            value = "/{coachingSessionId}/speech",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponseDTO<CoachingConversationDto.ResponseConversationTurn>> processUserSpeech(
            @PathVariable("coachingSessionId") Long coachingSessionId,
            @RequestPart("audioFile") MultipartFile audioFile
    ) {
        CoachingConversationDto.ResponseConversationTurn response =
                coachingConversationService.processUserSpeech(coachingSessionId, audioFile);

        return ResponseEntity.ok(
                ApiResponseDTO.success("사용자 음성 처리 성공", response)
        );
    }

    @Override
    @PostMapping("/{coachingSessionId}/finish")
    public ResponseEntity<ApiResponseDTO<CoachingConversationDto.ResponseFinishConversation>> finishConversation(
            @PathVariable("coachingSessionId") Long coachingSessionId
    ) {
        CoachingConversationDto.ResponseFinishConversation response =
                coachingConversationService.finishConversation(coachingSessionId);

        return ResponseEntity.ok(
                ApiResponseDTO.success("AI 코칭 최종 평가 완료", response)
        );
    }
}