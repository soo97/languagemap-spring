package kr.co.mapspring.learning.repository;

import kr.co.mapspring.learning.entity.UserGoal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGoalRepository extends JpaRepository<UserGoal, Long> {

    boolean existsByUserIdAndGoalMasterId(Long userId, Long goalMasterId);

}
