package cleaning.event;

import cleaning.type.CleaningType;
import cleaning.user.CleaningUser;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
public class CleaningEvent implements Serializable {
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(columnDefinition = "varchar(36)")
    @Id
    private String eventId;
    @Temporal(TemporalType.DATE)
    private Date eventDate;
    @Enumerated(EnumType.STRING)
    private CleaningEventStatus eventStatus;
    @ManyToOne
    private CleaningType cleaningType;
    @ManyToOne
    private CleaningUser cleaningUser;
}
