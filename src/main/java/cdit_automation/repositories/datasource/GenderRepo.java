package cdit_automation.repositories.datasource;

import cdit_automation.models.datasource.Gender;
import cdit_automation.models.datasource.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface GenderRepo extends JpaRepository<Gender, Long> {

  @Transactional
  @Modifying
  @Query(value = "TRUNCATE TABLE gender", nativeQuery = true)
  void truncateTable();

  @Transactional
  @Modifying
  @Query(
      value =
          "DELETE FROM GENDER del "
              + "WHERE del.ENTITY_KEY IN "
              + "(SELECT g.ENTITY_KEY FROM GENDER g "
              + "INNER JOIN PERSON_ID pid ON g.ENTITY_KEY = pid.ENTITY_KEY "
              + "WHERE pid.NATURAL_ID =:naturalId ORDER BY CAST(g.VALID_FROM AS DATE) DESC FETCH FIRST ROW ONLY)",
      nativeQuery = true)
  void deleteByNaturalId(@Param("naturalId") String naturalId);

  Gender findByPerson(Person person);

  @Query(
      value =
          "SELECT g.* FROM GENDER g "
              + "INNER JOIN PERSON_ID pid on g.ENTITY_KEY = pid.ENTITY_KEY "
              + "WHERE pid.NATURAL_ID =:naturalId "
              + "ORDER BY CAST(g.VALID_FROM AS DATE) DESC "
              + "FETCH FIRST ROW ONLY",
      nativeQuery = true)
  Gender findByNaturalId(@Param("naturalId") String naturalId);
}
