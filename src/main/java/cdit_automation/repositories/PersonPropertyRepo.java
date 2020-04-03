package cdit_automation.repositories;

import cdit_automation.models.Person;
import cdit_automation.models.PersonProperty;
import cdit_automation.models.Property;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PersonPropertyRepo extends JpaRepository<PersonProperty, Long> {

    @Query(value = "SELECT pp FROM PersonProperty pp WHERE " +
            "pp.identifier.personEntity = :#{#pp1.identifier.personEntity} AND " +
            "pp.batch.id != :#{#pp1.batch.id} AND " +
            "((pp.identifier.validFrom < :#{#pp1.identifier.validFrom} AND pp.validTill > :#{#pp1.identifier.validFrom}) OR " +
            "(pp.identifier.validFrom < :#{#pp1.identifier.validFrom} AND pp.validTill = :#{#pp1.identifier.validFrom}) OR " +
            "(pp.identifier.validFrom = :#{#pp1.identifier.validFrom} AND pp.validTill > :#{#pp1.identifier.validFrom}) )")
    PersonProperty findOverlappingRecord(@Param("pp1")PersonProperty newPersonProperty);

    @Query(value = "SELECT pp1 FROM PersonProperty pp1 WHERE " +
            "pp1.identifier.personEntity = :#{#new_pp.identifier.personEntity} AND " +
            "pp1.batch.id = :#{#new_pp.batch.id} AND " +
            "pp1.identifier.validFrom <= " +
                "( SELECT MIN(pp.identifier.validFrom) AS min_valid_from " +
                "FROM PersonProperty pp WHERE " +
                "pp.identifier.personEntity = :#{#new_pp.identifier.personEntity} AND " +
                "pp.batch.id != :#{#new_pp.batch.id} ) AND " +
            "pp1.validTill >= " +
                "( SELECT MAX(pp.validTill) AS max_valid_till " +
                "FROM PersonProperty pp WHERE " +
                "pp.identifier.personEntity = :#{#new_pp.identifier.personEntity} AND " +
                "pp.batch.id != :#{#new_pp.batch.id})")
    PersonProperty findErrorenousOverlap(@Param("new_pp")PersonProperty newPersonProperty);

    @Query(value = "SELECT pp.* FROM person_property AS OF PERIOD FOR validity_period_person_property TRUNC(sysdate) pp "
            + " WHERE pp.entity_key = ?1", nativeQuery = true)
    PersonProperty findByPerson(Person person);

    @Query(value = "SELECT pp.* FROM person_property AS OF PERIOD FOR validity_period_person_property TRUNC(sysdate) pp "
            + " WHERE pp.entity_key = ?1 AND pp.type = ?2", nativeQuery = true)
    PersonProperty findByPersonAndType(Person person, String type);

    @Query(value = "SELECT pp.* FROM person_property AS OF PERIOD FOR validity_period_person_property ?3 pp "
            + " WHERE pp.entity_key = ?1 AND pp.type = ?2", nativeQuery = true)
    PersonProperty findByPersonAndType(Person person, String type, Date date);

    @Query(value = "SELECT p.* FROM PERSON_PROPERTY AS OF PERIOD FOR validity_period_person_property TRUNC(SYSDATE) p " +
            "WHERE p.entity_key = ?1 AND p.property_entity_key = ?2 AND p.type = ?3", nativeQuery = true)
    PersonProperty findByPersonAndPropertyAndType(Person person, Property property, String type);

    @Query(value = "SELECT p.* FROM PERSON_PROPERTY AS OF PERIOD FOR validity_period_person_property ?4 p " +
            "WHERE p.entity_key = ?1 AND p.property_entity_key = ?2 AND p.type = ?3", nativeQuery = true)
    PersonProperty findByPersonAndPropertyAndType(Person person, Property property, String type, Date date);

    @Query(value = "SELECT p FROM PersonProperty p WHERE p.identifier.personEntity = ?1 AND p.identifier.type = 'RESIDENCE' ORDER BY p.identifier.validFrom DESC")
    List<PersonProperty> findAllResidencesByPersonOrderByValidFromDesc(Person person);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE PersonProperty pp SET pp.validTill = ?3 WHERE pp.identifier.personEntity = ?1 AND pp.identifier.propertyEntity = ?2")
    int updateValidTIll(Person person, Property property, Date date);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE PersonProperty pp SET pp.identifier.validFrom = ?3 WHERE pp.identifier.personEntity = ?1 AND pp.identifier.propertyEntity = ?2")
    int updateValidFrom(Person person, Property property, Date date);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE PersonProperty pp " +
            "SET pp.identifier.validFrom = ?3, pp.validTill = ?4 " +
            "WHERE pp.identifier.personEntity = ?1 AND pp.identifier.propertyEntity = ?2")
    int updateValidFromAndValidTill(Person person, Property property, Date validFrom, Date validTill);

    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE person_property", nativeQuery = true)
    void truncateTable();

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM PersonProperty pp WHERE " +
            "pp.identifier.personEntity = :#{#pp_record.identifier.personEntity} AND " +
            "pp.identifier.propertyEntity = :#{#pp_record.identifier.propertyEntity} AND " +
            "pp.batch = :#{#pp_record.batch} AND " +
            "pp.identifier.validFrom = :#{#pp_record.identifier.validFrom} AND " +
            "pp.validTill = :#{#pp_record.validTill}")
    int deleteByPersonAndPropertyAndBatch(@Param("pp_record") PersonProperty personProperty);
}
