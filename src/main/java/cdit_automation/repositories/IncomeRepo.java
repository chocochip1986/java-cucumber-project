package cdit_automation.repositories;

import cdit_automation.models.Batch;
import cdit_automation.models.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface IncomeRepo extends JpaRepository<Income, Long> {

  List<Income> findAllByBatch(Batch batch);

  @Transactional
  @Modifying
  @Query(value = "TRUNCATE TABLE income", nativeQuery = true)
  void truncateTable();
}
