package cleaning.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import java.io.Serializable;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Slf4j
public class CleaningUserContainer implements Serializable {
    @Getter
    CleaningUser user;
    @Autowired
    CleaningUserService cleaningUserService;
    @Autowired
    ObjectMapper objectMapper;

    @PostConstruct
    @SuppressWarnings("unchecked")
    public void init() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2Authentication auth = OAuth2Authentication.class.cast(authentication);
        this.user = this.cleaningUserService.save(this.retrieveUser(auth));
        log.info("load {}", this.user);
    }

    CleaningUser retrieveUser(OAuth2Authentication auth) throws Exception {
        OAuth2AuthenticationDetails details = OAuth2AuthenticationDetails.class.cast(auth.getDetails());
        String payload = details.getTokenValue().split("\\.")[1];
        JsonNode json = objectMapper.readValue(Base64Utils.decodeFromUrlSafeString(payload), JsonNode.class);
        String userId = json.get("user_id").asText();
        String email = json.get("user_name").asText();
        String displayName = json.get("display_name").asText();
        CleaningUser user = new CleaningUser(userId, email, displayName);
        return user;
    }
}
