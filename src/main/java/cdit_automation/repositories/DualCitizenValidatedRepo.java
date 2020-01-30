package cdit_automation.repositories;

import cdit_automation.models.DualCitizenValidated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DualCitizenValidatedRepo extends JpaRepository<DualCitizenValidated, Long> {
}
