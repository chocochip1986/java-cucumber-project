package cdit_automation.repositories;

import cdit_automation.models.NewCitizenValidated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewCitizenValidatedRepo extends JpaRepository<NewCitizenValidated, Long> {
}
