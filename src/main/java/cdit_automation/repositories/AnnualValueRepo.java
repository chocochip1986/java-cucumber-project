package cdit_automation.repositories;

import cdit_automation.models.AnnualValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnualValueRepo extends JpaRepository<AnnualValue, Long> {
}
