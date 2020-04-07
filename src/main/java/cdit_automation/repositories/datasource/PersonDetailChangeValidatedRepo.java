package cdit_automation.repositories.datasource;

import cdit_automation.models.PersonDetailChangeValidated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PersonDetailChangeValidatedRepo extends JpaRepository<PersonDetailChangeValidated, Long> {
    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE person_detail_change_validated", nativeQuery = true)
    void truncateTable();
}
