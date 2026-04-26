package kr.co.mapspring.ranking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RankingRepository extends JpaRepository<StudyLog, Long> {

    @Query("""
           SELECT s.userId, SUM(s.score)
           FROM StudyLog s
           GROUP BY s.userId
           ORDER BY SUM(s.score) DESC    
           """)
    List<Object[]> findRanking();
}
