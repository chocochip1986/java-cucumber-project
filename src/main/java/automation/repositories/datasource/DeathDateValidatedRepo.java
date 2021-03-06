package automation.repositories.datasource;

import automation.models.datasource.Batch;
import automation.models.datasource.DeathDateValidated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DeathDateValidatedRepo extends JpaRepository<DeathDateValidated, Long> {
    @Query
    DeathDateValidated findByNricAndBatch(String nric, Batch batch);

    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE death_date_validated", nativeQuery = true)
    void truncateTable();
}
