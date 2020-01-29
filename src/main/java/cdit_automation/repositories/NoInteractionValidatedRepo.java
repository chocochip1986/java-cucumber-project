package cdit_automation.repositories;

import cdit_automation.models.Batch;
import cdit_automation.models.NoInteractionValidated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoInteractionValidatedRepo extends JpaRepository<NoInteractionValidated, Long> {

    long countAllByBatch(Batch batch);
}
