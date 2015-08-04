package cleaning.event;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Getter
@ToString
public class CleaningEventSummary implements Serializable {
    private final long registeredCount;
    private final LocalDate eventDate;

    public CleaningEventSummary(long registeredCount, Date eventDate) {
        this.registeredCount = registeredCount;
        this.eventDate = ((java.sql.Date) eventDate).toLocalDate();
    }
}
