package cdit_automation.repositories;

import cdit_automation.models.Person;
import cdit_automation.models.PersonDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;

@Repository
public interface PersonDetailRepo extends JpaRepository<PersonDetail, Long> {
  @Query(
      "SELECT pd FROM PersonDetail pd "
          + "WHERE pd.person = ?1 "
          + "AND ( pd.biTemporalData.businessTemporalData.validFrom <= ?2 AND ( pd.biTemporalData.businessTemporalData.validTill = null OR pd.biTemporalData.businessTemporalData.validTill >= ?2 ) )")
  PersonDetail findCurrentPersonDetailByPerson(Person person, Date now);

    @Query(value = "SELECT p.* FROM PERSON_DETAIL AS OF PERIOD FOR validity_period_person_detail TRUNC(sysdate) p " +
            "WHERE p.entity_key = ?1", nativeQuery = true)
    PersonDetail findByPerson(Person person);

    @Query(value = "SELECT p.* FROM PERSON_DETAIL AS OF PERIOD FOR validity_period_person_detail ?2 p " +
            "WHERE p.entity_key = ?1", nativeQuery = true)
    PersonDetail findByPerson(Person person, Date date);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE PersonDetail p SET p.dateOfBirth = ?1 WHERE p.person = ?2")
    int updateBirthDateForPerson(LocalDate newBirthDate, Person person);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE PersonDetail p SET p.dateOfDeath = ?1 WHERE p.person = ?2")
    int updateDeathDateForPerson(LocalDate newDeathDate, Person person);
}
