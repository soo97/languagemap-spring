package kr.co.mapspring.learning.service;

public interface LearningGoalService {

    /**
     *
     * @param userId
     * @param goalmasterId
     */
    void selectGoal(Long userId, Long goalmasterId);
}
