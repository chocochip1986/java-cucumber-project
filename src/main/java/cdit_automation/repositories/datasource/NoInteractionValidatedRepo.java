package cdit_automation.repositories.datasource;

import cdit_automation.models.datasource.Batch;
import cdit_automation.models.datasource.NoInteractionValidated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface NoInteractionValidatedRepo extends JpaRepository<NoInteractionValidated, Long> {

    long countAllByBatch(Batch batch);

    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE no_interaction_validated", nativeQuery = true)
    void truncateTable();
}
