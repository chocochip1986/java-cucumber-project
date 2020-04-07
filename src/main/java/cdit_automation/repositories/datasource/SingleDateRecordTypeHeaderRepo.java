package cdit_automation.repositories.datasource;

import cdit_automation.models.SingleDateRecordTypeHeaderValidated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface SingleDateRecordTypeHeaderRepo
    extends JpaRepository<SingleDateRecordTypeHeaderValidated, Long> {

  @Transactional
  @Modifying
  @Query(value = "TRUNCATE TABLE single_date_record_type_header_validated", nativeQuery = true)
  void truncateTable();
}
