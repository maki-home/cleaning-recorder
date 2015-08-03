package cleaning.event;

import cleaning.type.CleaningTypeRepository;
import cleaning.user.CleaningUser;
import cleaning.user.CleaningUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;


@Service
@Transactional
public class CleaningEventService {
    @Autowired
    CleaningUserRepository cleaningUserRepository;
    @Autowired
    CleaningEventRepository cleaningEventRepository;
    @Autowired
    CleaningTypeRepository cleaningTypeRepository;

    public List<CleaningEvent> findAll() {
        return cleaningEventRepository.findAll();
    }

    public CleaningEvent done(Integer typeId, CleaningUser cleaningUser) {
        CleaningEvent event = new CleaningEvent();
        event.setEventStatus(CleaningEventStatus.DONE);
        event.setCleaningType(cleaningTypeRepository.findOne(typeId));
        event.setCleaningUser(cleaningUser);
        event.setEventDate(Date.valueOf(LocalDate.now()));
        return cleaningEventRepository.save(event);
    }
}
