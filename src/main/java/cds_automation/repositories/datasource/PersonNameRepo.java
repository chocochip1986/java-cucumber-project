package cds_automation.repositories.datasource;

import cds_automation.models.datasource.Person;
import cds_automation.models.datasource.PersonName;
import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PersonNameRepo extends JpaRepository<PersonName, Long> {

  @Transactional
  @Modifying(clearAutomatically = true)
  @Query(
      "UPDATE PersonName pn "
          + "SET pn.name = ?1 "
          + "WHERE pn.person = ?2 "
          + "AND ( pn.biTemporalData.businessTemporalData.validFrom <= TRUNC(SYSDATE) "
          + "AND ( pn.biTemporalData.businessTemporalData.validTill = null OR pn.biTemporalData.businessTemporalData.validTill >= TRUNC(SYSDATE) ) )")
  int updateNameForPerson(String name, Person person);

  @Transactional
  @Modifying(clearAutomatically = true)
  @Query(
      "UPDATE PersonName pn "
          + "SET pn.biTemporalData.businessTemporalData.validFrom = ?1 "
          + "WHERE pn.person = ?2 "
          + "AND ( pn.biTemporalData.businessTemporalData.validFrom = TRUNC(?3) )")
  int updateValidFrom(Date newValidFrom, Person person, Date oldValidFrom);

  @Query(
      value =
          "SELECT p.* FROM PERSON_NAME AS OF PERIOD FOR validity_period_person_name TRUNC(SYSDATE) p "
              + "WHERE p.entity_key = ?1",
      nativeQuery = true)
  PersonName findByPerson(Person person);

  @Transactional
  @Modifying
  @Query(value = "TRUNCATE TABLE person_name", nativeQuery = true)
  void truncateTable();

  @Transactional
  @Modifying
  @Query(
      value =
          "DELETE FROM PERSON_NAME del "
              + "WHERE del.ENTITY_KEY IN "
              + "(SELECT pn.ENTITY_KEY FROM PERSON_NAME pn "
              + "INNER JOIN PERSON_ID pid ON pn.ENTITY_KEY = pid.ENTITY_KEY "
              + "WHERE pid.NATURAL_ID =:naturalId ORDER BY CAST(pn.VALID_FROM AS DATE) DESC FETCH FIRST ROW ONLY)",
      nativeQuery = true)
  void deleteByNaturalId(@Param("naturalId") String naturalId);

  @Query(
      value =
          "SELECT pn.* FROM PERSON_NAME pn "
              + "INNER JOIN PERSON_ID pid on pn.ENTITY_KEY = pid.ENTITY_KEY "
              + "WHERE pid.NATURAL_ID =:naturalId "
              + "ORDER BY CAST(pn.VALID_FROM AS DATE) DESC "
              + "FETCH FIRST ROW ONLY",
      nativeQuery = true)
  PersonName findByNaturalId(@Param("naturalId") String naturalId);
}
