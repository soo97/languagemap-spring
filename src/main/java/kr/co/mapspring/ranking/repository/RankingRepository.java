package kr.co.mapspring.ranking.repository;

import kr.co.mapspring.learning.entity.StudyScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RankingRepository extends JpaRepository<StudyScore, Long> {

    @Query("""
           SELECT ss.studyLog.userId, SUM(ss.totalScore)
           FROM StudyScore ss
           GROUP BY ss.studyLog.userId
           ORDER BY SUM(ss.totalScore) DESC    
           """)
    List<Object[]> findRanking();
}
