package cleaning.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class CleaningEventCalendarController {
    @Autowired
    CleaningEventRepository cleaningEventRepository;
    @Autowired
    ObjectMapper objectMapper;

    @RequestMapping("/calendar")
    String calendar(Model model) throws JsonProcessingException {
        Map<String, Map<String, Long>> map = cleaningEventRepository.findSummary()
                .stream()
                .collect(Collectors.toMap(
                        x -> x.getEventDate().toString(),
                        x -> Collections.singletonMap("number", x.getRegisteredCount())));
        model.addAttribute("events", objectMapper.writeValueAsString(map));
        return "calendar";
    }

    @RequestMapping("/calendar/{localDate}")
    String date(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate localDate, Model model) {
        model.addAttribute("localDate", localDate);
        model.addAttribute("events", cleaningEventRepository.findByEventDate(Date.valueOf(localDate)));
        return "date";
    }
}
