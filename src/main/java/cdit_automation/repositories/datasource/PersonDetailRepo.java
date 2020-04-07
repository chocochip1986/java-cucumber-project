package cdit_automation.repositories.datasource;

import cdit_automation.models.datasource.Person;
import cdit_automation.models.datasource.PersonDetail;
import java.time.LocalDate;
import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PersonDetailRepo extends JpaRepository<PersonDetail, Long> {
  @Query(
      "SELECT pd FROM PersonDetail pd "
          + "WHERE pd.person = ?1 "
          + "AND ( pd.biTemporalData.businessTemporalData.validFrom <= ?2 AND ( pd.biTemporalData.businessTemporalData.validTill = null OR pd.biTemporalData.businessTemporalData.validTill >= ?2 ) )")
  PersonDetail findCurrentPersonDetailByPerson(Person person, Date now);

  @Query(
      value =
          "SELECT p.* FROM PERSON_DETAIL AS OF PERIOD FOR validity_period_person_detail TRUNC(sysdate) p "
              + "WHERE p.entity_key = ?1",
      nativeQuery = true)
  PersonDetail findByPerson(Person person);

  @Query(
      value =
          "SELECT p.* FROM PERSON_DETAIL AS OF PERIOD FOR validity_period_person_detail ?2 p "
              + "WHERE p.entity_key = ?1",
      nativeQuery = true)
  PersonDetail findByPerson(Person person, Date date);

  @Transactional
  @Modifying(clearAutomatically = true)
  @Query(
      "UPDATE PersonDetail p SET p.dateOfBirth = ?1, p.biTemporalData.businessTemporalData.validFrom =?1 WHERE p.person = ?2")
  int updateBirthDateForPerson(LocalDate newBirthDate, Person person);

  @Transactional
  @Modifying(clearAutomatically = true)
  @Query(
      "UPDATE PersonDetail pd "
          + "SET pd.dateOfDeath = ?1 "
          + "WHERE pd.person = ?2 "
          + "AND ( pd.biTemporalData.businessTemporalData.validFrom <= TRUNC(SYSDATE) "
          + "AND ( pd.biTemporalData.businessTemporalData.validTill = null OR pd.biTemporalData.businessTemporalData.validTill >= TRUNC(SYSDATE) ) )")
  int updateDeathDateForPerson(LocalDate newDeathDate, Person person);

  @Transactional
  @Modifying(clearAutomatically = true)
  @Query(
      "UPDATE PersonDetail pd "
          + "SET pd.dateOfDeath = ?1 "
          + "WHERE pd.person = ?2 "
          + "AND ( pd.biTemporalData.businessTemporalData.validFrom <= ?3 "
          + "AND ( pd.biTemporalData.businessTemporalData.validTill = null OR pd.biTemporalData.businessTemporalData.validTill >= ?3 ) )")
  int updateDeathDateForPerson(LocalDate newDeathDate, Person person, Date now);

  @Transactional
  @Modifying
  @Query(value = "TRUNCATE TABLE person_detail", nativeQuery = true)
  void truncateTable();

  @Transactional
  @Modifying
  @Query(
      value =
          "DELETE FROM PERSON_DETAIL del "
              + "WHERE del.ENTITY_KEY = "
              + "(SELECT pd.ENTITY_KEY FROM PERSON_DETAIL pd "
              + "INNER JOIN PERSON_ID pid ON pd.ENTITY_KEY = pid.ENTITY_KEY "
              + "WHERE pid.NATURAL_ID =:naturalId ORDER BY CAST(pd.VALID_FROM AS DATE) DESC FETCH FIRST ROW ONLY)",
      nativeQuery = true)
  void deleteByNaturalId(@Param("naturalId") String naturalId);

  @Query(
      value =
          "SELECT pd.* FROM PERSON_DETAIL pd "
              + "INNER JOIN PERSON_ID pid on pd.ENTITY_KEY = pid.ENTITY_KEY "
              + "WHERE pid.NATURAL_ID =:naturalId "
              + "ORDER BY CAST(pd.VALID_FROM AS DATE) DESC "
              + "FETCH FIRST ROW ONLY",
      nativeQuery = true)
  PersonDetail findByNaturalId(@Param("naturalId") String naturalId);
}
