package kr.co.mapspring.ai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kr.co.mapspring.ai.controller.docs.CoachingFlowControllerDocs;
import kr.co.mapspring.ai.dto.StartCoachingWithInitialMessageDto;
import kr.co.mapspring.ai.service.CoachingFlowService;
import kr.co.mapspring.global.dto.ApiResponseDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CoachingFlowController implements CoachingFlowControllerDocs {

    private final CoachingFlowService coachingFlowService;

    @Override
    @PostMapping("/api/coaching/flow/start")
    public ResponseEntity<ApiResponseDTO<StartCoachingWithInitialMessageDto.ResponseStartCoachingWithInitialMessage>> startCoachingWithInitialMessage(
            @RequestBody StartCoachingWithInitialMessageDto.RequestStartCoachingWithInitialMessage request
    ) {
        StartCoachingWithInitialMessageDto.ResponseStartCoachingWithInitialMessage response =
                coachingFlowService.startCoachingWithInitialMessage(request);

        return ResponseEntity.ok(
                ApiResponseDTO.success("AI 코칭 시작 및 초기 메시지 생성 성공", response)
        );
    }
}