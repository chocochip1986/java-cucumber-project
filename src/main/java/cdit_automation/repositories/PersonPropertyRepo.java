package cdit_automation.repositories;

import cdit_automation.models.PersonProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonPropertyRepo extends JpaRepository<PersonProperty, Long> {
}
