package kr.co.mapspring.learning.service;

import kr.co.mapspring.learning.enums.StudyType;

public interface LearningService {

    /**
     * 학습 완료 후 학습 기록과 점수를 저장한다.
     *
     * @param userId 사용자 ID
     * @param sessionId 학습 세션 ID
     * @param studyType 학습 유형
     * @param earnedExp 획득 경험치
     * @param naturalnessScore 문장 자연스러움 점수
     * @param fluencyScore 유창성 점수
     * @param totalScore 종합 점수
     */
    void recordStudyLog(Long userId, Long sessionId, StudyType studyType, Integer earnedExp, Integer naturalnessScore, Integer fluencyScore, Integer totalScore);
}
