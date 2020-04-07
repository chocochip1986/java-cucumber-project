package cdit_automation.repositories.datasource;

import cdit_automation.enums.PersonStatusTypeEnum;
import cdit_automation.models.Person;
import cdit_automation.models.PersonStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PersonStatusRepo extends JpaRepository<PersonStatus, Long> {

  PersonStatus getByPersonAndType(Person person, PersonStatusTypeEnum typeEnum);

  @Transactional
  @Modifying
  @Query(value = "TRUNCATE TABLE person_status", nativeQuery = true)
  void truncateTable();
}
