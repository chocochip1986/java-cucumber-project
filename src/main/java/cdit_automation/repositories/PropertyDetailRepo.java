package cdit_automation.repositories;

import cdit_automation.models.Property;
import cdit_automation.models.PropertyDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PropertyDetailRepo extends JpaRepository<PropertyDetail, Long> {
    @Query(value = "SELECT pd.* FROM PROPERTY_DETAIL AS OF PERIOD FOR validity_period_property_detail TRUNC(sysdate) pd " +
            "WHERE pd.entity_key = ?1", nativeQuery = true)
    PropertyDetail findByProperty(Property property);

    @Query(value = "SELECT pd FROM PropertyDetail pd WHERE " +
            "(pd.unit = ?1 OR (pd.unit IS NULL AND ?1 IS NULL)) AND " +
            "(pd.blockNumber = ?2 OR (pd.blockNumber IS NULL AND ?2 IS NULL)) AND " +
            "(pd.floor = ?3 OR (pd.floor IS NULL AND ?3 IS NULL)) AND " +
            "(pd.buildingName = ?4 OR (pd.buildingName IS NULL AND ?4 IS NULL)) AND " +
            "(pd.streetName = ?5 OR (pd.streetName IS NULL AND ?5 IS NULL)) AND " +
            "(pd.streetCode = ?6 OR (pd.streetCode IS NULL AND ?6 IS NULL)) AND " +
            "(pd.postalCode = ?7 OR (pd.postalCode IS NULL AND ?7 IS NULL)) AND " +
            "(pd.newPostalCode = ?8 OR (pd.newPostalCode IS NULL AND ?8 IS NULL))")
    List<PropertyDetail> findAllByAddress(String unit, String blockNo, String floor, String buildingName, String streetName, String streetCode, String postalCode, String newPostalCode);

    @Query(value = "SELECT pd FROM PropertyDetail pd WHERE " +
            "(pd.unit = ?1 OR (pd.unit IS NULL AND ?1 IS NULL)) AND " +
            "(pd.blockNumber = ?2 OR (pd.blockNumber IS NULL AND ?2 IS NULL)) AND " +
            "(pd.floor = ?3 OR (pd.floor IS NULL AND ?3 IS NULL)) AND " +
            "(pd.buildingName = ?4 OR (pd.buildingName IS NULL AND ?4 IS NULL)) AND " +
            "(pd.streetName = ?5 OR (pd.streetName IS NULL AND ?5 IS NULL)) AND " +
            "(pd.streetCode = ?6 OR (pd.streetCode IS NULL AND ?6 IS NULL)) AND " +
            "(pd.postalCode = ?7 OR (pd.postalCode IS NULL AND ?7 IS NULL)) AND " +
            "(pd.newPostalCode = ?8 OR (pd.newPostalCode IS NULL AND ?8 IS NULL))")
    PropertyDetail findByAddress(String unit, String blockNo, String floor, String buildingName, String streetName, String streetCode, String postalCode, String newPostalCode);

    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE property_detail", nativeQuery = true)
    void truncateTable();
}
