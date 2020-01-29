package cdit_automation.repositories;

import cdit_automation.enums.PersonStatusTypeEnum;
import cdit_automation.models.Person;
import cdit_automation.models.PersonStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonStatusRepo extends JpaRepository<PersonStatus, Long> {

  PersonStatus getByPersonAndType(Person person, PersonStatusTypeEnum typeEnum);
}
