package cdit_automation.repositories;

import cdit_automation.models.PersonDetailChangeValidated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonDetailChangeValidatedRepo extends JpaRepository<PersonDetailChangeValidated, Long> {
}
