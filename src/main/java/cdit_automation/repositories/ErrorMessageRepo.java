package cdit_automation.repositories;

import cdit_automation.models.Batch;
import cdit_automation.models.ErrorMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ErrorMessageRepo extends JpaRepository<ErrorMessage, Long> {
    List<ErrorMessage> findByBatch(Batch batch);

    @Query(value = "SELECT e.* FROM ERROR_MESSAGE e WHERE e.validated_id = ?1 AND e.validated_type = ?2", nativeQuery = true)
    ErrorMessage findByValidatedIdAndValidatedType(Long validatedKey, String validatedType);
}
