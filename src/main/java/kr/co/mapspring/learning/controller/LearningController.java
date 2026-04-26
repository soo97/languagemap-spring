package kr.co.mapspring.learning.controller;

import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.learning.controller.docs.LearningControllerDocs;
import kr.co.mapspring.learning.dto.LearningLogDto;
import kr.co.mapspring.learning.service.LearningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/learning")
@RequiredArgsConstructor
public class LearningController implements LearningControllerDocs {

    private LearningService learningService;

    @GetMapping("/logs")
    public ResponseEntity<ApiResponseDTO<List<LearningLogDto.ResponseLog>>> getStudyLogs(@RequestParam("userId") Long userId) {
        List<LearningLogDto.ResponseLog> result = learningService.getStudyLogs(userId);

        return ResponseEntity.ok(ApiResponseDTO.success("학습 기록 조회 성공", result));
    }
}
