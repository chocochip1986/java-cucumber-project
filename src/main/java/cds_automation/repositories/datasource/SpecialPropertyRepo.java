package cds_automation.repositories.datasource;

import cds_automation.models.datasource.Property;
import cds_automation.models.datasource.SpecialProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SpecialPropertyRepo extends JpaRepository<SpecialProperty, Long> {
    @Query(value = "SELECT sp.* FROM special_property AS OF PERIOD FOR validity_period_special_property TRUNC(SYSDATE) sp WHERE sp.property_entity_key = ?1", nativeQuery = true)
    SpecialProperty findByProperty(Property property);

    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE special_property", nativeQuery = true)
    int truncateTable();
}
