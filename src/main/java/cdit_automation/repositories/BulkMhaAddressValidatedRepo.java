package cdit_automation.repositories;

import cdit_automation.models.BulkMhaAddressValidated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BulkMhaAddressValidatedRepo extends JpaRepository<BulkMhaAddressValidated, Long> {
}
