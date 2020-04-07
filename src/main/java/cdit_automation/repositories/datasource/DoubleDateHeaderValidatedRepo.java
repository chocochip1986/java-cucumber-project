package cdit_automation.repositories.datasource;

import cdit_automation.models.DoubleDateHeaderValidated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DoubleDateHeaderValidatedRepo extends JpaRepository<DoubleDateHeaderValidated, Long> {
    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE double_date_header_validated", nativeQuery = true)
    void truncateTable();
}
