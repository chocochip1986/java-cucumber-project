package cdit_automation.repositories;

import cdit_automation.models.BulkNcaAddressValidated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BulkNcaAddressValidatedRepo extends JpaRepository<BulkNcaAddressValidated, Long> {
}
