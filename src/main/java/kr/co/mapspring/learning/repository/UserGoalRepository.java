package kr.co.mapspring.learning.repository;

import kr.co.mapspring.learning.entity.UserGoal;
import kr.co.mapspring.learning.enums.UserGoalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserGoalRepository extends JpaRepository<UserGoal, Long> {

    boolean existsByUserIdAndGoalMaster_GoalMasterId(Long userId, Long goalMasterId);

    int countByUserId(Long userId);

    List<UserGoal> findAllByUserIdAndStatus(Long userId, UserGoalStatus status);

}
