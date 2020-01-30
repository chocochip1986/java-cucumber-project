package cdit_automation.repositories;

import cdit_automation.models.NewMhaAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewMhaAddressRepo extends JpaRepository<NewMhaAddress, Long> {
}
