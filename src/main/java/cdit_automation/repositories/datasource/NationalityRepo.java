package cdit_automation.repositories.datasource;

import cdit_automation.enums.NationalityEnum;
import cdit_automation.models.Nationality;
import cdit_automation.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

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

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Nationality n " +
            "SET n.biTemporalData.businessTemporalData.validTill = ?1 " +
            "WHERE n.id = ?2")
    int updateValidTill(Date newValidFill, long id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Nationality n " +
            "SET n.nationality = ?1 " +
            "WHERE n.person = ?2 " +
            "AND ( n.biTemporalData.businessTemporalData.validFrom <= TRUNC(SYSDATE) " +
            "AND ( n.biTemporalData.businessTemporalData.validTill = null OR n.biTemporalData.businessTemporalData.validTill >= TRUNC(SYSDATE) ))")
    int updateNationality(NationalityEnum nationalityEnum, Person person);

  @Transactional
  @Modifying
  @Query(value = "TRUNCATE TABLE nationality", nativeQuery = true)
  void truncateTable();

  @Query(
      value =
          "SELECT nat.* FROM NATIONALITY nat "
              + "INNER JOIN PERSON_ID pid on nat.ENTITY_KEY = pid.ENTITY_KEY "
              + "WHERE pid.NATURAL_ID =:naturalId "
              + "ORDER BY CAST(nat.VALID_FROM AS DATE) DESC "
              + "FETCH FIRST ROW ONLY",
      nativeQuery = true)
  Nationality findByNaturalId(@Param("naturalId") String naturalId);

    @Query(nativeQuery = true,
    value = "SELECT pid.natural_id, nat.TYPE, nat.CITIZENSHIP_ATTAINMENT_DATE, nat.CITIZENSHIP_RENUNCIATION_DATE, nat.VALID_FROM, nat.VALID_TILL FROM Nationality nat INNER JOIN PERSON_ID pid ON nat.ENTITY_KEY = pid.ENTITY_KEY where pid.PERSON_ID_TYPE = 'NRIC'")
    List<?> getNationalityWithNric();
}