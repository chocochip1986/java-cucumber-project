package cdit_automation.repositories.datasource;

import cdit_automation.models.Batch;
import cdit_automation.models.Income;
import cdit_automation.models.interfaces.ICustomIncomeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncomeRepo extends JpaRepository<Income, Long> {

  List<Income> findAllByBatch(Batch batch);

  @Transactional
  @Modifying
  @Query(value = "TRUNCATE TABLE income", nativeQuery = true)
  void truncateTable();

  @Query(
      value =
          "SELECT PI.NATURAL_ID AS NATURALID, I.ASSESSABLE_INCOME AS ASSESSABLEINCOME, I.ASSESSABLE_INCOME_STATUS AS ASSESSABLEINCOMESTATUS "
              + "FROM INCOME AS OF PERIOD FOR VALIDITY_PERIOD_INCOME TO_DATE(?3, 'YYYY-MM-DD HH24:MI:SS') I "
              + "    JOIN PERSON P on I.ENTITY_KEY = P.PERSON_ID "
              + "    LEFT JOIN PERSON_ID PI on P.PERSON_ID = PI.ENTITY_KEY "
              + "WHERE I.YEAR = ?2 AND PI.NATURAL_ID IN (?1) AND I.ASSESSABLE_INCOME_STATUS <> 'NEW_APPEAL_CASE'",
      nativeQuery = true)
  List<ICustomIncomeRecord> findIncomeByNaturalIdsAndYearAndAsOf(
      List<String> naturalIds, int year, String dateStr);

  @Query(
      value =
          "SELECT I.* "
              + "FROM INCOME AS OF PERIOD FOR VALIDITY_PERIOD_INCOME TO_DATE(?3, 'YYYY-MM-DD HH24:MI:SS') I "
              + "JOIN PERSON P on I.ENTITY_KEY = P.PERSON_ID "
              + "LEFT JOIN PERSON_ID PI on P.PERSON_ID = PI.ENTITY_KEY "
              + "WHERE I.YEAR = ?2 AND PI.NATURAL_ID IN (?1)",
      nativeQuery = true)
  Optional<Income> findIncomeByNaturalIdAndYear(String naturalId, int year, String asOf);

  @Query(
      value =
          "SELECT I.* FROM INCOME I "
              + "    JOIN PERSON P on I.ENTITY_KEY = P.PERSON_ID "
              + "    LEFT JOIN PERSON_ID PI on P.PERSON_ID = PI.ENTITY_KEY "
              + "WHERE I.YEAR = ?2 AND PI.NATURAL_ID IN (?1) AND I.ASSESSABLE_INCOME_STATUS = 'NEW_APPEAL_CASE'",
      nativeQuery = true)
  Income findAppealIncomeByNaturalIdAndYear(String natural_id, String year);
}
