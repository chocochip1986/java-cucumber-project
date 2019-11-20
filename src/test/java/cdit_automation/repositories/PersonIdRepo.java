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

    @Query("SELECT p FROM PersonId p WHERE p.naturalId = ?1 AND p.personIdType = ?2 AND p.biTemporalData.businessTemporalData.validFrom <= ?3 AND ( p.biTemporalData.businessTemporalData.validTill = null OR p.biTemporalData.businessTemporalData.validTill >= ?3 )")
    PersonId findCurrentPersonIdByIdentifier(String identifier, PersonIdTypeEnum type, Date now);
}
