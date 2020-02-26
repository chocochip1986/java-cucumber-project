package cdit_automation.repositories;

import cdit_automation.models.Person;
import cdit_automation.models.PersonProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonPropertyRepo extends JpaRepository<PersonProperty, Long> {
    @Query(value = "SELECT pp.* FROM PersonProperty AS OF PERIOD FOR validity_period_person_property TRUNC(sysdate) pp"
            + "WHERE pp.entity_key = ?1 ", nativeQuery = true)
    PersonProperty findByPerson(Person person);
}
