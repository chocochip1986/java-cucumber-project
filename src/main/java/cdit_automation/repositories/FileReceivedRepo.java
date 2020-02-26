package cdit_automation.repositories;

import cdit_automation.enums.FileStatusEnum;
import cdit_automation.models.FileDetail;
import cdit_automation.models.FileReceived;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface FileReceivedRepo extends JpaRepository<FileReceived, Long> {
    @Query
    FileReceived findByFileDetailIdAndFileStatusEnumAndHash(Long fileDetailId, FileStatusEnum fileStatusEnum, String hash);

    @Query
    FileReceived findTopByFileDetailIdAndFileStatusEnumOrderByReceivedTimestampDesc(Long fileDetailId, FileStatusEnum fileStatusEnum);

    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE file_received", nativeQuery = true)
    void truncateTable();
}
