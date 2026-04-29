package kr.co.mapspring.learning.controller;

import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.learning.controller.docs.AdminLearningControllerDocs;
import kr.co.mapspring.learning.dto.AdminLearningDto;
import kr.co.mapspring.learning.entity.GoalMaster;
import kr.co.mapspring.learning.entity.StudyLog;
import kr.co.mapspring.learning.entity.StudyScore;
import kr.co.mapspring.learning.repository.StudyScoreRepository;
import kr.co.mapspring.learning.service.AdminLearningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/learning")
@RequiredArgsConstructor
public class AdminLearningController implements AdminLearningControllerDocs {

    private final AdminLearningService adminLearningService;
    private final StudyScoreRepository studyScoreRepository;

    @Override
    @GetMapping("/logs")
    public ResponseEntity<ApiResponseDTO<List<AdminLearningDto.ResponseStudyLog>>> getStudyLogs() {
        List<StudyLog> studyLogs = adminLearningService.getStudyLogs();

        List<AdminLearningDto.ResponseStudyLog> result = studyLogs.stream()
                .map(this::toResponseStudyLog)
                .toList();

        return ResponseEntity.ok(ApiResponseDTO.success("전체 학습 기록 조회 성공", result));
    }

    @Override
    @GetMapping("/logs/users/{userId}")
    public ResponseEntity<ApiResponseDTO<List<AdminLearningDto.ResponseStudyLog>>> getStudyLogsByUserId(
            @PathVariable("userId") Long userId
    ) {
        List<StudyLog> studyLogs = adminLearningService.getStudyLogsByUserId(userId);

        List<AdminLearningDto.ResponseStudyLog> result = studyLogs.stream()
                .map(this::toResponseStudyLog)
                .toList();

        return ResponseEntity.ok(ApiResponseDTO.success("사용자 학습 기록 조회 성공", result));
    }

    @Override
    @GetMapping("/goals")
    public ResponseEntity<ApiResponseDTO<List<AdminLearningDto.ResponseGoalMaster>>> getGoalMasters() {
        List<GoalMaster> goals = adminLearningService.getGoalMasters();

        List<AdminLearningDto.ResponseGoalMaster> result = goals.stream()
                .map(AdminLearningDto.ResponseGoalMaster::from)
                .toList();

        return ResponseEntity.ok(ApiResponseDTO.success("학습 목표 목록 조회 성공", result));
    }

    @Override
    @PatchMapping("/goals/{goalMasterId}/active")
    public ResponseEntity<ApiResponseDTO<Void>> updateGoalActive(
            @PathVariable("goalMasterId") Long goalMasterId,
            @RequestBody AdminLearningDto.RequestUpdateGoalActive request
    ) {
        adminLearningService.updateGoalActive(goalMasterId, request.isActive());

        return ResponseEntity.ok(ApiResponseDTO.success("학습 목표 활성 상태 변경 성공", null));
    }

    private AdminLearningDto.ResponseStudyLog toResponseStudyLog(StudyLog studyLog) {
        StudyScore studyScore = studyScoreRepository
                .findByStudyLog_StudyLogId(studyLog.getStudyLogId())
                .orElse(null);

        return AdminLearningDto.ResponseStudyLog.from(studyLog, studyScore);
    }
}