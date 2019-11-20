package cdit_automation.repositories;

import cdit_automation.models.Person;
import cdit_automation.models.PersonDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface PersonDetailRepo extends JpaRepository<PersonDetail, Long> {

    @Query("SELECT p FROM PersonDetail p " +
            "WHERE p.person = ?1 " +
            "AND p.biTemporalData.businessTemporalData.validFrom <= ?2 " +
            "AND ( p.biTemporalData.businessTemporalData.validTill = null OR p.biTemporalData.businessTemporalData.validTill >= ?2 )")
    PersonDetail findByPerson(Person person, Date now);
}
