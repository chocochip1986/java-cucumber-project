package automation.repositories.datasource;

import automation.models.datasource.ChangeAddressValidated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ChangeAddressValidatedRepo extends JpaRepository<ChangeAddressValidated, Long> {
    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE change_address_validated", nativeQuery = true)
    void truncateTable();
}
