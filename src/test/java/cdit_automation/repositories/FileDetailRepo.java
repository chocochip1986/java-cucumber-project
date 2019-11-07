package cdit_automation.repositories;

import cdit_automation.enums.FileTypeEnum;
import cdit_automation.models.FileDetail;
import cdit_automation.models.FileReceived;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDetailRepo extends JpaRepository<FileDetail, Long> {
    FileDetail findByFileEnum(FileTypeEnum fileTypeEnum);
}