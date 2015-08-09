package cleaning.event;

import cleaning.type.CleaningType;
import cleaning.user.CleaningUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@IdClass(CleaningEvent.EventId.class)
public class CleaningEvent implements Serializable {
    @Id
    @Temporal(TemporalType.DATE)
    private Date eventDate;

    @Id
    @ManyToOne
    private CleaningType cleaningType;

    @Enumerated(EnumType.STRING)
    private CleaningEventStatus eventStatus;

    @ManyToOne
    private CleaningUser cleaningUser;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EventId implements Serializable {
        private Date eventDate;
        private Integer cleaningType;
    }
}
