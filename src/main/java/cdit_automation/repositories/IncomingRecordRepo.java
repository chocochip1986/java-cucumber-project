package cdit_automation.repositories;


import cdit_automation.models.IncomingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface IncomingRecordRepo extends JpaRepository<IncomingRecord, Long> {
    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE incoming_record", nativeQuery = true)
    void truncateTable();
}
