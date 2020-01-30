package cdit_automation.repositories;

import cdit_automation.models.ChangeAddressValidated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangeAddressValidatedRepo extends JpaRepository<ChangeAddressValidated, Long> {
}
