package cdit_automation.repositories;

import cdit_automation.models.Person;
import cdit_automation.models.PersonDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface PersonDetailRepo extends JpaRepository<PersonDetail, Long> {
  @Query(
      "SELECT pd FROM PersonDetail pd "
          + "WHERE pd.person = ?1 "
          + "AND ( pd.biTemporalData.businessTemporalData.validFrom <= ?2 AND ( pd.biTemporalData.businessTemporalData.validTill = null OR pd.biTemporalData.businessTemporalData.validTill >= ?2 ) )")
  PersonDetail findCurrentPersonDetailByPerson(Person person, Date now);
}
