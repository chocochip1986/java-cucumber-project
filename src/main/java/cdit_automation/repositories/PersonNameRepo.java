package cdit_automation.repositories;

import cdit_automation.models.Person;
import cdit_automation.models.PersonName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Repository
public interface PersonNameRepo extends JpaRepository<PersonName, Long> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE PersonName pn " +
            "SET pn.name = ?1 " +
            "WHERE pn.person = ?2 " +
            "AND ( pn.biTemporalData.businessTemporalData.validFrom <= TRUNC(SYSDATE) " +
            "AND ( pn.biTemporalData.businessTemporalData.validTill = null OR pn.biTemporalData.businessTemporalData.validTill >= TRUNC(SYSDATE) ) )")
    int updateNameForPerson(String name, Person person);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE PersonName pn " +
            "SET pn.biTemporalData.businessTemporalData.validFrom = ?1 " +
            "WHERE pn.person = ?2 " +
            "AND ( pn.biTemporalData.businessTemporalData.validFrom = TRUNC(?3) )")
    int updateValidFrom(Date newValidFrom, Person person, Date oldValidFrom);

    @Query(value = "SELECT p.* FROM PERSON_NAME AS OF PERIOD FOR validity_period_person_name TRUNC(SYSDATE) p " +
            "WHERE p.entity_key = ?1", nativeQuery = true)
    PersonName findByPerson(Person person);

    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE person_name", nativeQuery = true)
    void truncateTable();
}
