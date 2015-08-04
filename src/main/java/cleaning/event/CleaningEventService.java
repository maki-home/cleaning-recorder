package cleaning.event;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import lombok.Getter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cleaning.type.CleaningTypeRepository;
import cleaning.user.CleaningUser;
import cleaning.user.CleaningUserRepository;

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
        event.setEventStatus(status);
        event.setCleaningType(cleaningTypeRepository.findOne(typeId));
        event.setCleaningUser(cleaningUser);
        event.setEventDate(Date.valueOf(date));
        try {
            return cleaningEventRepository.saveAndFlush(event);
        } catch (DataIntegrityViolationException e) {
            throw new CleaningEventAlreadyRegisteredException(event, e);
        }
    }

    public static class CleaningEventAlreadyRegisteredException extends
                                                               RuntimeException {
        @Getter
        private final CleaningEvent cleaningEvent;

        public CleaningEventAlreadyRegisteredException(
                CleaningEvent cleaningEvent, Exception e) {
            super(e);
            this.cleaningEvent = cleaningEvent;
        }

        @Override
        public String getMessage() {
            return cleaningEvent.getCleaningType().getTypeName() + " @ "
                    + cleaningEvent.getEventDate() + " is already registered.";
        }
    }
}
