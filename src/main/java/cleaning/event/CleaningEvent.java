package cleaning.event;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import lombok.Data;

import org.hibernate.annotations.GenericGenerator;

import cleaning.type.CleaningType;
import cleaning.user.CleaningUser;

@Entity
@Data
@Table(indexes = { @Index(unique = true, columnList = "eventDate, cleaning_type_typeId") })
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
