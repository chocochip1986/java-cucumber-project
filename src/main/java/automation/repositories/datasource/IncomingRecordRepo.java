package automation.repositories.datasource;

import automation.models.datasource.Batch;
import automation.models.datasource.IncomingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface IncomingRecordRepo extends JpaRepository<IncomingRecord, Long> {
    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE incoming_record", nativeQuery = true)
    void truncateTable();

    @Query(value = "SELECT ir FROM IncomingRecord ir WHERE ir.batch = ?1 AND ir.fileContentCode = 'BODY'")
    List<IncomingRecord> findAllBodyByBatch(Batch batch);
    
    long countAllByBatch(Batch batch);
}
