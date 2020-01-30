package cdit_automation.repositories;

import cdit_automation.models.DoubleDateHeaderValidated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoubleDateHeaderValidatedRepo extends JpaRepository<DoubleDateHeaderValidated, Long> {
}
