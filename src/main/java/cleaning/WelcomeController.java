package cleaning;

import cleaning.user.CleaningUserContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {
    @Autowired
    CleaningUserContainer userContainer;

    @RequestMapping("/")
    Object hello() {
        return userContainer.getUser();
    }
}
