package cds_automation.repositories.datasource;

import cds_automation.models.datasource.Batch;
import cds_automation.models.datasource.FileReceived;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BatchRepo extends JpaRepository<Batch, Long> {

    //findByFileReceivedOrderByCreatedAtDesc
    Batch findFirstByFileReceivedOrderByCreatedAtDesc(FileReceived fileReceived);

    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE batch", nativeQuery = true)
    void truncateTable();
}
