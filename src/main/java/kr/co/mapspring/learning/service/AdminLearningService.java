package kr.co.mapspring.learning.service;

import kr.co.mapspring.learning.entity.GoalMaster;
import kr.co.mapspring.learning.entity.StudyLog;

import java.util.List;

public interface AdminLearningService {

    /**
     * 관리자가 전체 학습 기록을 조회한다.
     *
     * 모든 사용자의 학습 기록을 조회한다.
     *
     * @return 전체 학습 기록 목록
     */
    List<StudyLog> getStudyLogs();

    /**
     * 관리자가 특정 사용자의 학습 기록을 조회한다.
     *
     * userId에 해당하는 사용자의 학습 기록만 조회한다.
     *
     * @param userId 사용자 ID
     * @return 특정 사용자의 학습 기록 목록
     */
    List<StudyLog> getStudyLogsByUserId(Long userId);

    /**
     * 관리자가 전체 학습 목표 목록을 조회한다.
     *
     * 활성화/비활성화 여부와 관계없이 모든 목표를 조회한다.
     *
     * @return 전체 학습 목표 목록
     */
    List<GoalMaster> getGoalMasters();

    /**
     * 관리자가 학습 목표의 활성화 상태를 변경한다.
     *
     * 목표를 삭제하지 않고 isActive 값을 변경한다.
     *
     * @param goalMasterId 상태를 변경할 목표 ID
     * @param active 활성화 여부
     */
    void updateGoalActive(Long goalMasterId, boolean active);
}
