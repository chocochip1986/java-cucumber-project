package cdit_automation.repositories;

import cdit_automation.enums.NationalityEnum;
import cdit_automation.enums.PersonIdTypeEnum;
import cdit_automation.models.PersonId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PersonIdRepo extends JpaRepository<PersonId, Long> {
    PersonId findByNaturalId ( String identifier );

    @Query("SELECT p FROM PersonId p, Nationality n " +
            "WHERE p.person IS NOT NULL AND n.person IS NOT NULL " +
            "AND p.person = n.person AND p.naturalId = ?1 " +
            "AND n.nationality = 'DUAL_CITIZENSHIP' " +
            "AND p.biTemporalData.businessTemporalData.validFrom <= ?2 AND ( p.biTemporalData.businessTemporalData.validTill = null OR p.biTemporalData.businessTemporalData.validTill >= ?3 ) " +
            "AND ( n.biTemporalData.businessTemporalData.validFrom <= ?2 AND ( n.biTemporalData.businessTemporalData.validTill = null OR n.biTemporalData.businessTemporalData.validTill >= ?3 ) )")
    List<PersonId> findDualCitizen (String identifier, Date validFrom, Date validTill);

    @Query("SELECT p FROM PersonId p WHERE p.naturalId = ?1 AND p.personIdType = ?2 AND p.biTemporalData.businessTemporalData.validFrom <= ?3 AND ( p.biTemporalData.businessTemporalData.validTill = null OR p.biTemporalData.businessTemporalData.validTill >= ?3 )")
    PersonId findCurrentPersonIdByIdentifier(String identifier, PersonIdTypeEnum type, Date now);
}
