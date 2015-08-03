package cleaning.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class CleaningUserService {
    @Autowired
    CleaningUserRepository cleaningUserRepository;

    public CleaningUser save(CleaningUser user) {
        return this.cleaningUserRepository.save(user);
    }
}
