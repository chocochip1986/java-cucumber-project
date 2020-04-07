package cdit_automation.repositories.datasource;

import cdit_automation.models.datasource.Batch;
import cdit_automation.models.datasource.ReasonablenessCheckStatistic;
import java.sql.Timestamp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ReasonablenessCheckStatisticRepo extends JpaRepository<ReasonablenessCheckStatistic, Long> {
    @Query
    ReasonablenessCheckStatistic findByBatchAndDataItemAndDataCollectedDate(Batch batch, String dateItem, Timestamp dataCollectedDate);

    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE reasonableness_check_statistic", nativeQuery = true)
    void truncateTable();
}
