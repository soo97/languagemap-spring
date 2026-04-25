package kr.co.mapspring.learning.service;

import kr.co.mapspring.learning.dto.LearningLogDto;
import kr.co.mapspring.learning.enums.StudyType;

import java.util.List;

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

    /**
     * 사용자의 학습 기록 목록을 조회한다.
     *
     * @param userId 사용자 ID
     * @return 학습 기록 목록
     */
    List<LearningLogDto.ResponseLog> getStudyLogs(Long userId);
}
