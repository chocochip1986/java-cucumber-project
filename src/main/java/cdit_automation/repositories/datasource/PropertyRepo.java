package cdit_automation.repositories.datasource;

import cdit_automation.models.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PropertyRepo extends JpaRepository<Property, Long> {
    @Query
    Property findByPropertyId(Long id);

    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE property", nativeQuery = true)
    void truncateTable();
}
