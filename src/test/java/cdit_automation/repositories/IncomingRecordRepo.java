package cdit_automation.repositories;


import cdit_automation.models.IncomingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncomingRecordRepo extends JpaRepository<IncomingRecord, Long> {
}