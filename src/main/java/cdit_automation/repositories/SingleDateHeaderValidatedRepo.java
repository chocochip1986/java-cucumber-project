package cdit_automation.repositories;

import cdit_automation.models.SingleDateHeaderValidated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SingleDateHeaderValidatedRepo extends JpaRepository<SingleDateHeaderValidated, Long> {
}
