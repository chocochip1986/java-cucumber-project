package cdit_automation.repositories;

import cdit_automation.models.OldMhaAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OldMhaAddressRepo extends JpaRepository<OldMhaAddress, Long> {
}
