package cdit_automation.repositories;

import cdit_automation.models.PersonDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonDetailRepo extends JpaRepository<PersonDetail, Long> {
}
