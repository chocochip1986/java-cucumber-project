package automation.repositories.datasource;

import automation.models.datasource.JobExecution;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BatchJobExecutionRepo extends JpaRepository<JobExecution, Long> {
  Optional<JobExecution> findById(Long id);

  @Transactional
  @Modifying
  @Query(value = "TRUNCATE TABLE batch_job_execution", nativeQuery = true)
  void truncateTable();
}
