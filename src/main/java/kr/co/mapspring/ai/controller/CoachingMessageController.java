package kr.co.mapspring.ai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.mapspring.ai.controller.docs.CoachingMessageControllerDocs;
import kr.co.mapspring.ai.dto.CoachingMessageDto;
import kr.co.mapspring.ai.service.CoachingMessageService;
import kr.co.mapspring.global.dto.ApiResponseDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/coaching/messages")
@RequiredArgsConstructor
public class CoachingMessageController implements CoachingMessageControllerDocs {

    // AI 코칭 메시지 서비스
    private final CoachingMessageService coachingMessageService;

    @Override
    @PostMapping
    public ResponseEntity<ApiResponseDTO<CoachingMessageDto.ResponseCoachingMessage>> saveCoachingMessage(
            @RequestBody CoachingMessageDto.RequestSaveCoachingMessage request
    ) {
        // 사용자 또는 AI 메시지 저장
        CoachingMessageDto.ResponseCoachingMessage response =
                coachingMessageService.saveCoachingMessage(request);

        // 공통 성공 응답 반환
        return ResponseEntity.ok(
                ApiResponseDTO.success("AI 코칭 메시지 저장 성공", response)
        );
    }

    @Override
    @GetMapping("/{coachingSessionId}")
    public ResponseEntity<ApiResponseDTO<CoachingMessageDto.ResponseGetCoachingMessages>> getCoachingMessages(
            @PathVariable ("CoachingSessionId")Long coachingSessionId
    ) {
        // 코칭 세션 기준 메시지 목록 조회
        CoachingMessageDto.ResponseGetCoachingMessages response =
                coachingMessageService.getCoachingMessages(coachingSessionId);

        // 공통 성공 응답 반환
        return ResponseEntity.ok(ApiResponseDTO.success("AI 코칭 메시지 목록 조회 성공", response));
    }
}