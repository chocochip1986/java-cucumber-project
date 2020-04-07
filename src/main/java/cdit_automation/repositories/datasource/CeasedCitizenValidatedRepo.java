package cdit_automation.repositories.datasource;

import cdit_automation.models.CeasedCitizenValidated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CeasedCitizenValidatedRepo extends JpaRepository<CeasedCitizenValidated, Long> {
    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE ceased_citizen_validated", nativeQuery = true)
    void truncateTable();
}
