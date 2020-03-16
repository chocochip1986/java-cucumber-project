package cdit_automation.repositories;

import cdit_automation.models.SpecialMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SpecialMappingRepo extends JpaRepository<SpecialMapping, Long> {
    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE special_mapping", nativeQuery = true)
    int truncateTable();
}
