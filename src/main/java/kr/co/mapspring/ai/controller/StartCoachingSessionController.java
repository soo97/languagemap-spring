package kr.co.mapspring.ai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.mapspring.ai.controller.docs.StartCoachingSessionControllerDocs;
import kr.co.mapspring.ai.dto.StartCoachingSessionDto;
import kr.co.mapspring.ai.service.StartCoachingSessionService;
import kr.co.mapspring.global.dto.ApiResponseDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/coaching/sessions")
@RequiredArgsConstructor
public class StartCoachingSessionController implements StartCoachingSessionControllerDocs {

    // AI 코칭 세션 시작 서비스
    private final StartCoachingSessionService startCoachingSessionService;

    @Override
    @PostMapping
    public ResponseEntity<ApiResponseDTO<StartCoachingSessionDto.ResponseStartCoachingSession>> startCoachingSession(
            @RequestBody StartCoachingSessionDto.RequestStartCoachingSession request
    ) {
        // 코칭 세션 시작 또는 기존 진행 중 세션 조회
        StartCoachingSessionDto.ResponseStartCoachingSession response =
                startCoachingSessionService.startCoachingSession(request);

        // 공통 성공 응답 반환
        return ResponseEntity.ok(ApiResponseDTO.success("AI 코칭 세션 시작 성공", response));
    }
}