package cleaning.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Lazy
@Component
public class InstalledDate implements Serializable {
    private Date installedOn;
    @Autowired
    JdbcTemplate jdbcTemplate;

    public InstalledDate(Date installedOn) {
        this.installedOn = installedOn;
    }

    public InstalledDate() {

    }

    public Date asDate() {
        return this.installedOn;
    }

    public LocalDateTime asLocalDateTime() {
        return LocalDateTime.ofInstant(this.installedOn.toInstant(), ZoneId.systemDefault());
    }

    public LocalDate asLocalDate() {
        return this.asLocalDateTime().toLocalDate();
    }

    @PostConstruct
    void init() {
        this.installedOn = jdbcTemplate.queryForObject("SELECT \"installed_on\" FROM \"public\".\"schema_version\" ORDER BY \"installed_on\" ASC LIMIT 1", Date.class);
    }
}
