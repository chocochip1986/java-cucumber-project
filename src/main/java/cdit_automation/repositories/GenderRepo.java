package cdit_automation.repositories;

import cdit_automation.models.Gender;
import cdit_automation.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface GenderRepo extends JpaRepository<Gender, Long> {

    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE gender", nativeQuery = true)
    void truncateTable();

    Gender findByPerson(Person person);
}
