package automation.repositories.datasource;

import automation.models.datasource.CeasedCitizenValidated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CeasedCitizenRepo extends JpaRepository<CeasedCitizenValidated, Long> {

}
