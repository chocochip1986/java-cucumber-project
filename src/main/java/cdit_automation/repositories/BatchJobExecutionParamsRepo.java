package cdit_automation.repositories;


import cdit_automation.models.JobExecutionParams;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchJobExecutionParamsRepo extends JpaRepository<JobExecutionParams, Long> {
  Optional<JobExecutionParams> findFirstByKeyNameAndLongValOrderByIdDesc(String keyName, Long val);
}
