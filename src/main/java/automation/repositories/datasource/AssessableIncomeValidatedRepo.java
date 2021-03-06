package automation.repositories.datasource;

import automation.models.datasource.AssessableIncomeValidated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface AssessableIncomeValidatedRepo
    extends JpaRepository<AssessableIncomeValidated, Long> {

  @Transactional
  @Modifying
  @Query(value = "TRUNCATE TABLE assessable_income_validated", nativeQuery = true)
  void truncateTable();
}
