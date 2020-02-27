package cdit_automation.repositories;

import cdit_automation.models.NewNcaAddress;
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
