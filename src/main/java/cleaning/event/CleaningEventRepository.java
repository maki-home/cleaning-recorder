package cleaning.event;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CleaningEventRepository extends JpaRepository<CleaningEvent, CleaningEvent.EventId> {
    @Query("SELECT NEW cleaning.event.CleaningEventSummary(COUNT(x), x.eventDate) FROM CleaningEvent x GROUP BY x.eventDate")
    List<CleaningEventSummary> findSummary();

    List<CleaningEvent> findByEventDate(Date eventDate);

    Optional<CleaningEvent> findFirstByCleaningType_TypeIdOrderByEventDateDesc(Integer typeId);
}
