package cds_automation.repositories.datasource;

import cds_automation.enums.datasource.FileTypeEnum;
import cds_automation.models.datasource.FileDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDetailRepo extends JpaRepository<FileDetail, Long> {
    FileDetail findByFileEnum(FileTypeEnum fileTypeEnum);
}
