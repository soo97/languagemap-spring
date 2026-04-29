package kr.co.mapspring.ranking.repository;

import kr.co.mapspring.learning.entity.StudyScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface RankingRepository extends JpaRepository<StudyScore, Long> {

    @Query("""
           SELECT ss.studyLog.user.userId, SUM(ss.totalScore)
           FROM StudyScore ss
           GROUP BY ss.studyLog.user.userId
           ORDER BY SUM(ss.totalScore) DESC    
           """)
    List<Object[]> findRanking();

    @Query("""
           SELECT ss.studyLog.user.userId, SUM(ss.totalScore)
           FROM StudyScore ss
           WHERE ss.studyLog.user.userId IN :friendIds
           GROUP BY ss.studyLog.user.userId
           ORDER BY SUM(ss.totalScore) DESC
           """)
    List<Object[]> findFriendRanking(List<Long> friendIds);

    @Query("""
           SELECT COALESCE(MAX(ss.totalScore), 0)
           FROM StudyScore ss
           WHERE ss.studyLog.user.userId IN :friendIds
           """)
    Long findFriendBestScore(List<Long> friendIds);

    @Query("""
           SELECT COALESCE(AVG(ss.totalScore), 0)
           FROM StudyScore ss
           WHERE ss.studyLog.user.userId IN :friendIds
           """)
    Double findFriendAverageScore(List<Long> friendIds);

    @Query("""
       SELECT ss.studyLog.user.userId, SUM(ss.totalScore)
       FROM StudyScore ss
       WHERE ss.studyLog.learningSession.endTime >= :startDateTime
       GROUP BY ss.studyLog.user.userId
       ORDER BY SUM(ss.totalScore) DESC
       """)
    List<Object[]> findWeeklyRanking(LocalDateTime startDateTime);
}
