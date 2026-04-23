package kr.co.mapspring.learning.service;

import kr.co.mapspring.learning.entity.UserGoal;

import java.util.List;

public interface LearningGoalService {
    /**
     * 사용자가 학습 목표를 선택한다.
     *
     * - 동일한 목표는 중복 선택할 수 없다.
     * - 최대 3개의 목표까지만 선택 가능하다.
     *
     * @param userId 사용자 ID
     * @param goalMasterId 선택할 목표 ID
     */
    void selectGoal(Long userId, Long goalMasterId);

    /**
     * 사용자가 선택한 학습 목표를 해제한다.
     *
     * 목표를 삭제하지 않고 상태를 CANCELED로 변경한다.
     *
     * @param userGoalId 사용자 목표 ID
     */
    void cancelGoal(Long userGoalId);

    /**
     * 사용자의 진행 중인 학습 목표 목록을 조회한다.
     *
     * 진행 중 상태(ACTIVE)인 목표만 조회한다.
     *
     * @param userId 사용자 ID
     * @return 진행 중인 사용자 목표 목록
     */
    List<UserGoal> getActiveGoals(Long userId);
}
