package cdit_automation.repositories;

import cdit_automation.models.CeasedCitizenValidated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CeasedCitizenValidatedRepo extends JpaRepository<CeasedCitizenValidated, Long> {
}
