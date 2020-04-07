package cds_automation.repositories.datasource;

import cds_automation.models.datasource.NewMhaAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface NewMhaAddressRepo extends JpaRepository<NewMhaAddress, Long> {
    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE new_mha_address_validated", nativeQuery = true)
    void truncateTable();
}
