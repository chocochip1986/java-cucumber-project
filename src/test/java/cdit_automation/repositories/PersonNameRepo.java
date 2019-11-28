package cdit_automation.repositories;

import cdit_automation.models.PersonName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonNameRepo extends JpaRepository<PersonName, Long> {
}
