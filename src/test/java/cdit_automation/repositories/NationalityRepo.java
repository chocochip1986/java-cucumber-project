package cdit_automation.repositories;

import cdit_automation.models.Nationality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NationalityRepo extends JpaRepository<Nationality, Long> {
}
