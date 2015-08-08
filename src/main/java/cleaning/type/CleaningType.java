package cleaning.type;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
public class CleaningType implements Serializable {
    @Id
    @Max(99)
    @Min(0)
    private Integer typeId;
    @Size(min = 1, max = 50)
    @NotNull
    private String typeName;
    @Max(999)
    @Min(0)
    @NotNull
    private Integer cycleDay;

    public LocalDate limitDate(LocalDate lastDate) {
        return lastDate.plusDays(cycleDay);
    }
}
