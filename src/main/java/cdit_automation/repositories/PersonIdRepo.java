package cdit_automation.repositories;

import cdit_automation.models.Person;
import cdit_automation.models.PersonId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface PersonIdRepo extends JpaRepository<PersonId, Long> {
    PersonId findByNaturalId ( String identifier );

    @Query(value = "SELECT p.* FROM PERSON_ID AS OF PERIOD FOR validity_period_person_id TRUNC(SYSDATE) p, NATIONALITY AS OF PERIOD FOR validity_period_nationality TRUNC(SYSDATE) n " +
            "WHERE p.entity_key IS NOT NULL AND n.entity_key IS NOT NULL " +
            "AND p.entity_key = n.entity_key AND p.natural_id = ?1 " +
            "AND n.type = 'DUAL_CITIZENSHIP'", nativeQuery = true)
    List<PersonId> findDualCitizen (String identifier);

    @Query(value = "SELECT p.* FROM PERSON_ID AS OF PERIOD FOR validity_period_person_id ?2 p, NATIONALITY AS OF PERIOD FOR validity_period_nationality ?2 n " +
            "WHERE p.entity_key IS NOT NULL AND n.entity_key IS NOT NULL " +
            "AND p.entity_key = n.entity_key AND p.natural_id = ?1 " +
            "AND n.type = 'DUAL_CITIZENSHIP'", nativeQuery = true)
    List<PersonId> findDualCitizen (String identifier, Date date);

    @Query(value = "SELECT p.* FROM PERSON_ID AS OF PERIOD FOR validity_period_person_id TRUNC(SYSDATE) p " +
            "WHERE p.natural_id = ?1", nativeQuery = true)
    PersonId findPersonByNaturalId(String identifier);

    @Query(value = "SELECT p.* FROM PERSON_ID AS OF PERIOD FOR validity_period_person_id ?2 p " +
            "WHERE p.natural_id = ?1", nativeQuery = true)
    PersonId findPersonByNaturalId(String identifier, Date date);

    @Query(value = "SELECT p.* FROM PERSON_ID AS OF PERIOD FOR validity_period_person_id TRUNC(SYSDATE) p, PERSON_DETAIL AS OF PERIOD FOR validity_period_person_detail TRUNC(SYSDATE) pd" +
            " WHERE p.entity_key IS NOT NULL AND pd.entity_key IS NOT NULL" +
            " AND p.entity_key = pd.entity_key" +
            " AND p.natural_id IS NOT NULL" +
            " AND p.person_id_type IN (NRIC, FIN, PP)" +
            " AND ( pd.date_of_death IS NOT NULL OR pd.date_of_death > TRUNC(ADD_MONTHS(SYSDATE,-12),'YEAR')-1 )", nativeQuery = true)
    List<PersonId> findAllPeopleForIrasAIBulkFile();

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE PersonId pi " +
            "SET pi.biTemporalData.businessTemporalData.validFrom = ?1 " +
            "WHERE pi.person = ?2 " +
            "AND ( pi.biTemporalData.businessTemporalData.validFrom = TRUNC(?3) )")
    int updateValidFrom(Date newValidFrom, Person person, Date oldValidFrom);

    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE person_id", nativeQuery = true)
    void truncateTable();
}
