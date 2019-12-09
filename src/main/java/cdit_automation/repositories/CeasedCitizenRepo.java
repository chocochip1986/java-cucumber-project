package cdit_automation.repositories;

import cdit_automation.models.CeasedCitizen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CeasedCitizenRepo extends JpaRepository<CeasedCitizen, Long> {

}
