package kr.co.mapspring.learning.service;

import kr.co.mapspring.learning.entity.GoalMaster;
import kr.co.mapspring.learning.entity.UserGoal;
import kr.co.mapspring.learning.enums.GoalType;

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

    /**
     * 사용자의 진행 중인 목표 진행도를 갱신한다.
     *
     * 목표 타입이 일치하는 ACTIVE 상태의 목표를 찾아 진행도를 반영한다.
     * 목표 달성 시 상태를 COMPLETED로 변경한다.
     *
     * @param userId 사용자 ID
     * @param goalType 목표 타입
     * @param amount 증가시킬 진행도 값
     */
    void updateGoalProgress(Long userId, GoalType goalType, Integer amount);

    /**
     * 사용자의 완료된 학습 목표 목록을 조회한다.
     *
     * 완료 상태(COMPLETED)인 목표만 조회한다.
     *
     * @param userId 사용자 ID
     * @return 완료된 사용자 목표 목록
     */
    List<UserGoal> getCompletedGoals(Long userId);

    /**
     * 사용자가 선택할 수 있는 학습 목표 목록을 조회한다.
     *
     * - 활성화된 목표 중에서
     * - 이미 선택한 목표를 제외한 목록을 반환한다.
     *
     * @param userId 사용자 ID
     * @return 선택 가능한 목표 목록
     */
    List<GoalMaster> getAvailableGoals(Long userId);
}
