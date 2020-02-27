package cdit_automation.repositories;

import cdit_automation.models.AnnualValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AnnualValueRepo extends JpaRepository<AnnualValue, Long> {
    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE annual_value", nativeQuery = true)
    void truncateTable();
}
