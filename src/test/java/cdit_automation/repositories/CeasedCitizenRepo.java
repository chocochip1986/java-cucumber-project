package cdit_automation.repositories;

import cdit_automation.models.CeasedCitizenValidated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CeasedCitizenRepo extends JpaRepository<CeasedCitizenValidated, Long> {

}
