package cdit_automation.repositories;

import cdit_automation.models.Person;
import cdit_automation.models.PersonDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface PersonDetailRepo extends JpaRepository<PersonDetail, Long> {

    @Query(value = "SELECT p.* FROM PERSON_DETAIL AS OF PERIOD FOR validity_period_person_detail TRUNC(sysdate) p " +
            "WHERE p.entity_key = ?1", nativeQuery = true)
    PersonDetail findByPerson(Person person);

    @Query(value = "SELECT p.* FROM PERSON_DETAIL AS OF PERIOD FOR validity_period_person_detail ?2 p " +
            "WHERE p.entity_key = ?1", nativeQuery = true)
    PersonDetail findByPerson(Person person, Date date);
}
