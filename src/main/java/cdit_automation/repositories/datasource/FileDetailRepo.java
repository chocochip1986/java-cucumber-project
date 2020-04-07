package cdit_automation.repositories.datasource;

import cdit_automation.enums.FileTypeEnum;
import cdit_automation.models.FileDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDetailRepo extends JpaRepository<FileDetail, Long> {
    FileDetail findByFileEnum(FileTypeEnum fileTypeEnum);
}
