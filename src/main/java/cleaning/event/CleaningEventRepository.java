package cleaning.event;


import org.springframework.data.jpa.repository.JpaRepository;

public interface CleaningEventRepository extends JpaRepository<CleaningEvent, String> {
}
