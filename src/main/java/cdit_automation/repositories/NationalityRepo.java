package cdit_automation.repositories;

import cdit_automation.models.Nationality;
import cdit_automation.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Repository
public interface NationalityRepo extends JpaRepository<Nationality, Long> {

    @Query(value = "SELECT n.* FROM NATIONALITY AS OF PERIOD FOR validity_period_nationality TRUNC(SYSDATE) n " +
            "WHERE n.entity_key = ?1", nativeQuery = true)
    Nationality findNationalityByPerson(Person person);

    @Query(value = "SELECT n.* FROM NATIONALITY AS OF PERIOD FOR validity_period_nationality ?2 n " +
            "WHERE n.entity_key = ?1", nativeQuery = true)
    Nationality findNationalityByPerson(Person person, Date date);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Nationality n " +
            "SET n.biTemporalData.businessTemporalData.validFrom = ?1 " +
            "WHERE n.person = ?2 " +
            "AND ( n.biTemporalData.businessTemporalData.validFrom = TRUNC(?3) )")
    int updateValidFrom(Date newValidFrom, Person person, Date oldValidFrom);
}
