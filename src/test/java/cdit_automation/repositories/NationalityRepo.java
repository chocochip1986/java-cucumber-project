package cdit_automation.repositories;

import cdit_automation.models.Nationality;
import cdit_automation.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface NationalityRepo extends JpaRepository<Nationality, Long> {
    Nationality findByPerson(Person person);

    @Query("SELECT n FROM Nationality n " +
            "WHERE n.person = ?1 " +
            "AND ( n.biTemporalData.businessTemporalData.validFrom <= ?2 AND ( n.biTemporalData.businessTemporalData.validTill = null OR n.biTemporalData.businessTemporalData.validTill >= ?2 ) )")
    Nationality findCurrentNationalityByPerson(Person person, Date now);
}
