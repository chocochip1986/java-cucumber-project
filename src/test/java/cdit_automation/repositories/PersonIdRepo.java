package cdit_automation.repositories;

import cdit_automation.models.PersonId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonIdRepo extends JpaRepository<PersonId, Long> {
    public PersonId findByNaturalId ( String identifier );
}
