package cdit_automation.repositories;

import cdit_automation.models.ReasonablenessCheckStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ReasonablenessCheckStatisticRepo extends JpaRepository<ReasonablenessCheckStatistic, Long> {
    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE reasonableness_check_statistic", nativeQuery = true)
    void truncateTable();
}
