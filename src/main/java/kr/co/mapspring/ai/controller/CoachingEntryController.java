package kr.co.mapspring.ai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.mapspring.ai.controller.docs.CoachingEntryControllerDocs;
import kr.co.mapspring.ai.dto.CoachingEntryDto;
import kr.co.mapspring.ai.service.CoachingEntryService;
import kr.co.mapspring.global.dto.ApiResponseDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/coaching/entry")
@RequiredArgsConstructor
public class CoachingEntryController implements CoachingEntryControllerDocs {

    // AI 코칭 진입 서비스
    private final CoachingEntryService coachingEntryService;

    @Override
    @GetMapping("/{sessionId}")
    public ResponseEntity<ApiResponseDTO<CoachingEntryDto.ResponseGetCoachingEntry>> getCoachingEntryData(
            @PathVariable("sessionId") Long sessionId
    ) {
        // 진입 데이터 조회
        CoachingEntryDto.ResponseGetCoachingEntry response =
                coachingEntryService.getCoachingEntryData(sessionId);

        // 공통 성공 응답 반환
        return ResponseEntity.ok(ApiResponseDTO.success("AI 코칭 진입 데이터 조회 성공", response));
    }
}