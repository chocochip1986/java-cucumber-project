package cdit_automation.repositories.datasource;

import cdit_automation.models.datasource.SingleDateHeaderValidated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SingleDateHeaderValidatedRepo extends JpaRepository<SingleDateHeaderValidated, Long> {
    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE single_date_header_validated", nativeQuery = true)
    void truncateTable();
}
