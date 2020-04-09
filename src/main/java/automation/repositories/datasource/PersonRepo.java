package automation.repositories.datasource;

import automation.models.datasource.Person;
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
