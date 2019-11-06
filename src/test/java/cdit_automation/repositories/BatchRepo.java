package cdit_automation.repositories;

import cdit_automation.models.Batch;
import cdit_automation.models.FileReceived;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchRepo extends JpaRepository<Batch, Long> {

    List<Batch> findByFileReceivedOrderByCreatedAtDesc(FileReceived fileReceived);
}
