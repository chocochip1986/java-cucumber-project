package cdit_automation.repositories;

import cdit_automation.models.FileReceived;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileReceivedRepo extends JpaRepository<FileReceived, Long> {
}
