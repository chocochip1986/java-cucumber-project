package automation.repositories.datasource;

import automation.models.datasource.DualCitizenValidated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DualCitizenValidatedRepo extends JpaRepository<DualCitizenValidated, Long> {
    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE dual_citizen_validated", nativeQuery = true)
    void truncateTable();
}
