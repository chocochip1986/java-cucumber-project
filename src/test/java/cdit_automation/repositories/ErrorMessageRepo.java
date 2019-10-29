package cdit_automation.repositories;

import cdit_automation.models.ErrorMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrorMessageRepo extends JpaRepository<ErrorMessage, Long> {
}
