package kr.co.mapspring.learning.repository;

import kr.co.mapspring.learning.entity.GoalMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoalMasterRepository extends JpaRepository<GoalMaster, Long> {

    List<GoalMaster> findAllByIsActiveTrue();
}
