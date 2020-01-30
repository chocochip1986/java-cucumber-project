package cdit_automation.repositories;

import cdit_automation.models.BulkCitizenValidated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BulkCitizenValidatedRepo extends JpaRepository<BulkCitizenValidated, Long> {
}
