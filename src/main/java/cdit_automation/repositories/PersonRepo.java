package cdit_automation.repositories;

import cdit_automation.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PersonRepo extends JpaRepository<Person, Long> {
    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE person", nativeQuery = true)
    void truncateTable();
}
