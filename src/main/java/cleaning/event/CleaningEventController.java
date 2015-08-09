package cleaning.event;

import cleaning.type.CleaningType;
import cleaning.type.CleaningTypeService;
import cleaning.user.CleaningUserContainer;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class CleaningEventController {
    @Autowired
    CleaningUserContainer cleaningUserContainer;

    @Autowired
    CleaningEventService cleaningEventService;

    @Autowired
    CleaningTypeService cleaningTypeService;

    @ModelAttribute
    CleaningEventRegistrationForm form() {
        CleaningEventRegistrationForm form = new CleaningEventRegistrationForm();
        form.setEventDate(LocalDate.now());
        return form;
    }

    @ModelAttribute("types")
    List<CleaningType> types() {
        return cleaningTypeService.findAll();
    }

    @RequestMapping("/")
    String home(Model model) {
        model.addAttribute("events", cleaningEventService.findAll());
        return "index";
    }

    @RequestMapping(value = "/done", method = RequestMethod.POST)
    String home(CleaningEventRegistrationForm form, Model model) {
        cleaningEventService.done(form.getTypeId(), cleaningUserContainer
                .getUser(), form.getEventDate());
        return "redirect:/";
    }

    @ResponseBody
    @RequestMapping("register")
    List<CleaningEvent> register(@RequestParam Integer typeId) {
        cleaningEventService.done(typeId, cleaningUserContainer.getUser(),
                LocalDate.now());
        return cleaningEventService.findAll();
    }

    @ResponseBody
    @RequestMapping("skip")
    List<CleaningEvent> skip(@RequestParam Integer typeId) {
        cleaningEventService.skip(typeId, cleaningUserContainer.getUser(),
                LocalDate.now());
        return cleaningEventService.findAll();
    }

    @Data
    public static class CleaningEventRegistrationForm {
        Integer typeId;

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate eventDate;
    }
}
