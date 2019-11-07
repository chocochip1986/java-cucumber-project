package cdit_automation.repositories;

import cdit_automation.models.Batch;
import cdit_automation.models.ErrorMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ErrorMessageRepo extends JpaRepository<ErrorMessage, Long> {
    List<ErrorMessage> findByBatch(Batch batch);
}
