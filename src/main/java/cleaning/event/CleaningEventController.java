package cleaning.event;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import cleaning.type.CleaningType;
import cleaning.type.CleaningTypeService;
import cleaning.user.CleaningUserContainer;

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

    @ExceptionHandler(CleaningEventService.CleaningEventAlreadyRegisteredException.class)
    ModelAndView registeredError(
            CleaningEventService.CleaningEventAlreadyRegisteredException e) {
        return new ModelAndView("index")
                .addObject(form())
                .addObject("types", types())
                .addObject("error", e.getMessage())
                .addObject("events", cleaningEventService.findAll());
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
