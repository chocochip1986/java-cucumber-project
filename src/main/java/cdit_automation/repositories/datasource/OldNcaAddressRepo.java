package cdit_automation.repositories.datasource;

import cdit_automation.models.datasource.OldNcaAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OldNcaAddressRepo extends JpaRepository<OldNcaAddress, Long> {
    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE old_nca_address_validated", nativeQuery = true)
    void truncateTable();
}
