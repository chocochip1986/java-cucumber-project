package cdit_automation.repositories;

import cdit_automation.enums.FileStatusEnum;
import cdit_automation.models.FileReceived;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FileReceivedRepo extends JpaRepository<FileReceived, Long> {
    @Query
    FileReceived findByFileDetailIdAndFileStatusEnumAndHash(Long fileDetailId, FileStatusEnum fileStatusEnum, String hash);
}
