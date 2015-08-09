package cleaning.event;

import cleaning.type.CleaningTypeRepository;
import cleaning.user.CleaningUser;
import cleaning.user.CleaningUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "eventDate"))
                .and(new Sort(new Sort.Order(Sort.Direction.ASC, "eventStatus")))
                .and(new Sort(new Sort.Order(Sort.Direction.ASC, "cleaningType.typeId")));
        return cleaningEventRepository.findAll(sort);
    }

    public List<CleaningEventSummary> findSummary() {
        return cleaningEventRepository.findSummary();
    }

    public List<CleaningEvent> findByEventDate(LocalDate localDate) {
        return cleaningEventRepository.findByEventDate(Date.valueOf(localDate));
    }

    public Optional<LocalDate> findLastEventDate(Integer typeId) {
        return cleaningEventRepository.findFirstByCleaningType_TypeIdOrderByEventDateDesc(typeId)
                .map(e -> Date.class.cast(e.getEventDate()).toLocalDate());
    }

    public CleaningEvent done(Integer typeId, CleaningUser cleaningUser,
                              LocalDate date) {
        return register(typeId, cleaningUser, CleaningEventStatus.DONE, date);
    }

    public CleaningEvent skip(Integer typeId, CleaningUser cleaningUser,
                              LocalDate date) {
        return register(typeId, cleaningUser, CleaningEventStatus.SKIP, date);
    }

    CleaningEvent register(Integer typeId, CleaningUser cleaningUser,
                           CleaningEventStatus status, LocalDate date) {
        CleaningEvent event = new CleaningEvent();
        event.setEventDate(Date.valueOf(date));
        event.setCleaningType(cleaningTypeRepository.findOne(typeId));
        event.setEventStatus(status);
        event.setCleaningUser(cleaningUser);
        System.out.println(event);
        return cleaningEventRepository.saveAndFlush(event);
    }
}
