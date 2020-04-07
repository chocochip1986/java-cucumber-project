package cdit_automation.repositories.datasource;

import cdit_automation.models.datasource.NewNcaAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface NewNcaAddressRepo extends JpaRepository<NewNcaAddress, Long> {
    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE new_nca_address_validated", nativeQuery = true)
    void truncateTable();
}
