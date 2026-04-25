package kr.co.mapspring.learning.controller;

import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.learning.controller.docs.LearningGoalControllerDocs;
import kr.co.mapspring.learning.dto.LearningGoalDto;
import kr.co.mapspring.learning.entity.GoalMaster;
import kr.co.mapspring.learning.entity.UserGoal;
import kr.co.mapspring.learning.service.LearningGoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/learning/goals")
@RequiredArgsConstructor
public class LearningGoalController implements LearningGoalControllerDocs {

    private final LearningGoalService learningGoalService;

    @Override
    @PostMapping
    public ResponseEntity<ApiResponseDTO<Void>> selectGoal(@RequestBody LearningGoalDto.RequestSelectGoal request) {
        learningGoalService.selectGoal(
                request.getUserId(),
                request.getGoalMasterId()
        );

        return ResponseEntity.ok(ApiResponseDTO.success("학습 목표 선택 성공"));
    }

    @Override
    @DeleteMapping("/{userGoalId}")
    public ResponseEntity<ApiResponseDTO<Void>> cancelGoal(@PathVariable("userGoalId") Long userGoalId) {
        learningGoalService.cancelGoal(userGoalId);

        return ResponseEntity.ok(ApiResponseDTO.success("학습 목표 해제 성공"));
    }

    @Override
    @GetMapping("/active")
    public ResponseEntity<ApiResponseDTO<List<LearningGoalDto.ResponseUserGoal>>> getActiveGoals(@RequestParam("userId") Long userId) {
        List<UserGoal> goals = learningGoalService.getActiveGoals(userId);

        List<LearningGoalDto.ResponseUserGoal> result = goals.stream()
                .map(LearningGoalDto.ResponseUserGoal::from)
                .toList();

        return ResponseEntity.ok(ApiResponseDTO.success("진행 중 목표 조회 성공", result));
    }

    @Override
    @GetMapping("completed")
    public ResponseEntity<ApiResponseDTO<List<LearningGoalDto.ResponseUserGoal>>> getCompletedGoals(@RequestParam("userId") Long userId) {
        List<UserGoal> goals = learningGoalService.getCompletedGoals(userId);

        List<LearningGoalDto.ResponseUserGoal> result = goals.stream()
                .map(LearningGoalDto.ResponseUserGoal::from)
                .toList();

        return ResponseEntity.ok(ApiResponseDTO.success("완료 목표 조회 성공", result));
    }

    @Override
    @GetMapping("/available")
    public ResponseEntity<ApiResponseDTO<List<LearningGoalDto.ResponseGoalMaster>>> getAvailableGoals(@RequestParam("userId") Long userId) {
        List<GoalMaster> goals = learningGoalService.getAvailableGoals(userId);

        List<LearningGoalDto.ResponseGoalMaster> result = goals.stream()
                .map(LearningGoalDto.ResponseGoalMaster::from)
                .toList();

        return ResponseEntity.ok(ApiResponseDTO.success("선택 가능 목표 조회 성공", result));
    }
}
