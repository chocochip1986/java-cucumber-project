package cdit_automation.repositories;

import cdit_automation.models.NewNcaAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewNcaAddressRepo extends JpaRepository<NewNcaAddress, Long> {
}
