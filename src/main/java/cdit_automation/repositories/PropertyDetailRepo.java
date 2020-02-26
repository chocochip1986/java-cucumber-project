package cdit_automation.repositories;

import cdit_automation.models.Property;
import cdit_automation.models.PropertyDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PropertyDetailRepo extends JpaRepository<PropertyDetail, Long> {
    @Query(value = "SELECT pd.* FROM PROPERTY_DETAIL AS OF PERIOD FOR validity_period_property_detail TRUNC(sysdate) pd " +
            "WHERE pd.entity_key = ?1", nativeQuery = true)
    PropertyDetail findByProperty(Property property);

    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE property_detail", nativeQuery = true)
    void truncateTable();
}
