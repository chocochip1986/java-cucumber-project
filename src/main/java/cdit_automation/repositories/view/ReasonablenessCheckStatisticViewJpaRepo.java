package cdit_automation.repositories.view;

import cdit_automation.models.view.ReasonablenessCheckStatisticView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ReasonablenessCheckStatisticViewJpaRepo extends JpaRepository<ReasonablenessCheckStatisticView, Long> {
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM ReasonablenessCheckStatisticView")
    void deleteAll();
}
