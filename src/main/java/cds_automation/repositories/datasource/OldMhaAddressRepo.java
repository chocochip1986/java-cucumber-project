package cds_automation.repositories.datasource;

import cds_automation.models.datasource.OldMhaAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OldMhaAddressRepo extends JpaRepository<OldMhaAddress, Long> {
    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE old_mha_address_validated", nativeQuery = true)
    void truncateTable();
}
