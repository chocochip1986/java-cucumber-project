package cdit_automation.repositories;

import cdit_automation.models.Batch;
import cdit_automation.models.FileReceived;
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
