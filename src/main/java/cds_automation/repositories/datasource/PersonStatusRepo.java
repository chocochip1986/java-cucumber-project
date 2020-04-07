package cds_automation.repositories.datasource;

import cds_automation.enums.datasource.PersonStatusTypeEnum;
import cds_automation.models.datasource.Person;
import cds_automation.models.datasource.PersonStatus;
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
