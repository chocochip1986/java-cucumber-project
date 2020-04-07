package cdit_automation.repositories.datasource;

import cdit_automation.models.datasource.PropertyValidated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PropertyValidatedRepo extends JpaRepository<PropertyValidated, Long> {
    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE property_validated", nativeQuery = true)
    void truncateTable();
}
