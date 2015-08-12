package cleaning.event;

import cleaning.type.CleaningType;
import cleaning.type.CleaningTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class CleaningEventLimitController {
    @Autowired
    CleaningTypeService cleaningTypeService;
    @Autowired
    CleaningEventService cleaningEventService;

    @RequestMapping("/limits")
    String limits(Model model) {
        List<CleaningType> types = cleaningTypeService.findAll();
        List<CleaningEventService.TypeNameAndLimit> limits = cleaningEventService
                .findTypeNameAndLimitOverLimit(types);
        model.addAttribute("limits", limits);
        return "limits";
    }
}
