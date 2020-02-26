package cdit_automation.repositories;

import cdit_automation.models.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepo extends JpaRepository<Property, Long> {
    @Query
    Property findByPropertyId(Long id);
}
