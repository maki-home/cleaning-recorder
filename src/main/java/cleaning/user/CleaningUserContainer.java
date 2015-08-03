package cleaning.user;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Slf4j
public class CleaningUserContainer implements Serializable {
    @Getter
    CleaningUser user;
    @Autowired
    CleaningUserService cleaningUserService;

    @PostConstruct
    @SuppressWarnings("unchecked")
    public void init() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2Authentication auth = OAuth2Authentication.class.cast(authentication);
        this.user = this.cleaningUserService.save(this.retrieveUser(auth));
        log.info("load {}", this.user);
    }

    CleaningUser retrieveUser(OAuth2Authentication auth) {
        Map<String, Object> details = (Map<String, Object>) auth.getUserAuthentication().getDetails();
        String userId = auth.getName();
        String email = (String) ((Map<String, Object>) ((List) details.get("emails")).get(0)).get("value");
        String displayName = (String) details.get("displayName");
        CleaningUser user = new CleaningUser(userId, email, displayName);
        return user;
    }
}
