package cdit_automation.repositories;

import cdit_automation.models.FileDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDetailRepo extends JpaRepository<FileDetail, Long> {
}
