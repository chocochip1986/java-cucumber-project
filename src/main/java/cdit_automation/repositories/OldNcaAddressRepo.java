package cdit_automation.repositories;

import cdit_automation.models.OldNcaAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OldNcaAddressRepo extends JpaRepository<OldNcaAddress, Long> {
}
