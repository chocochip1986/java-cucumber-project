package cdit_automation.repositories;

import cdit_automation.enums.PersonPropertyTypeEnum;
import cdit_automation.models.Person;
import cdit_automation.models.PersonProperty;
import cdit_automation.models.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Repository
public interface PersonPropertyRepo extends JpaRepository<PersonProperty, Long> {
    @Query(value = "SELECT pp.* FROM person_property AS OF PERIOD FOR validity_period_person_property TRUNC(sysdate) pp "
            + " WHERE pp.entity_key = ?1", nativeQuery = true)
    PersonProperty findByPerson(Person person);

    @Query(value = "SELECT pp.* FROM person_property AS OF PERIOD FOR validity_period_person_property TRUNC(sysdate) pp "
            + " WHERE pp.entity_key = ?1 AND pp.type = ?2", nativeQuery = true)
    PersonProperty findByPersonAndType(Person person, PersonPropertyTypeEnum type);

    @Query(value = "SELECT p.* FROM PERSON_PROPERTY AS OF PERIOD FOR validity_period_person_property TRUNC(SYSDATE) p " +
            "WHERE p.entity_key = ?1 AND p.property_entity_key = ?2 AND p.type = ?3", nativeQuery = true)
    PersonProperty findByPersonAndPropertyAndType(Person person, Property property, PersonPropertyTypeEnum type);

    @Query(value = "SELECT p.* FROM PERSON_PROPERTY AS OF PERIOD FOR validity_period_person_property ?4 p " +
            "WHERE p.entity_key = ?1 AND p.property_entity_key = ?2 AND p.type = ?3", nativeQuery = true)
    PersonProperty findByPersonAndPropertyAndType(Person person, Property property, PersonPropertyTypeEnum type, Date date);

    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE person_property", nativeQuery = true)
    void truncateTable();
}
