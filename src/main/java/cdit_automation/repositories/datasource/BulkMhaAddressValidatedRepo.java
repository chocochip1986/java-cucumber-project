package cdit_automation.repositories.datasource;

import cdit_automation.models.datasource.BulkMhaAddressValidated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BulkMhaAddressValidatedRepo extends JpaRepository<BulkMhaAddressValidated, Long> {
    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE bulk_mha_address_validated", nativeQuery = true)
    void truncateTable();
}
