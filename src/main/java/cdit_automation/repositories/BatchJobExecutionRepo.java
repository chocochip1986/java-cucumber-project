package cdit_automation.repositories;

import cdit_automation.models.JobExecution;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchJobExecutionRepo extends JpaRepository<JobExecution, Long> {
  Optional<JobExecution> findById(Long id);
}
