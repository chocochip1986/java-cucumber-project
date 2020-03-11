package cdit_automation.repositories;

import cdit_automation.models.SingleDateNoOfRecordsHeaderValidated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SingleDateNoOfRecordsHeaderValidatedRepo extends JpaRepository<SingleDateNoOfRecordsHeaderValidated, Long> {
    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE single_date_no_of_records_header_validated", nativeQuery = true)
    void truncateTable();
}
