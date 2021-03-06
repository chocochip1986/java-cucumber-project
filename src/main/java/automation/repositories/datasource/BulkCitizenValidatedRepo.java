package automation.repositories.datasource;

import automation.models.datasource.Batch;
import automation.models.datasource.BulkCitizenValidated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BulkCitizenValidatedRepo extends JpaRepository<BulkCitizenValidated, Long> {
    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE bulk_citizen_validated", nativeQuery = true)
    void truncateTable();

    long countByBatch(Batch batch);
}
