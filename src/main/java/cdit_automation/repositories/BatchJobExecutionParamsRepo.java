package cdit_automation.repositories;


import cdit_automation.models.JobExecutionParams;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BatchJobExecutionParamsRepo extends JpaRepository<JobExecutionParams, Long> {
  Optional<JobExecutionParams> findFirstByKeyNameAndLongValOrderByIdDesc(String keyName, Long val);

  @Transactional
  @Modifying
  @Query(value = "TRUNCATE TABLE batch_job_execution_params", nativeQuery = true)
  void truncateTable();
}
