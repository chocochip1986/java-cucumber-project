package cdit_automation.repositories;

import cdit_automation.models.Person;
import cdit_automation.models.PersonName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
}
