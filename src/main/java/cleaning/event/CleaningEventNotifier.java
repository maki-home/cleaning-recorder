package cleaning.event;

import cleaning.system.InstalledDate;
import cleaning.type.CleaningType;
import cleaning.type.CleaningTypeRepository;
import cleaning.user.CleaningUser;
import cleaning.user.CleaningUserContainer;
import cleaning.user.CleaningUserRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@RestController
@Slf4j
public class CleaningEventNotifier {
    @Autowired
    JavaMailSender mailSender;
    @Autowired
    SpringTemplateEngine templateEngine;
    @Autowired
    InstalledDate installedDate;
    @Autowired
    CleaningUserContainer cleaningUserContainer;
    @Autowired
    CleaningTypeRepository cleaningTypeRepository;
    @Autowired
    CleaningEventRepository cleaningEventRepository;
    @Autowired
    CleaningUserRepository cleaningUserRepository;

    BlockingQueue<MimeMessagePreparator> messages = new LinkedBlockingQueue<>(100);

    void reserveToSend(List<TypeNameAndLimit> limits) {
        Context context = new Context();
        context.setVariable("user", cleaningUserContainer.getUser().getDisplayName());
        context.setVariable("limits", limits);
        List<CleaningUser> users = cleaningUserRepository.findAll();

        MimeMessagePreparator preparator = (mimeMessage) -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setFrom("noreply@ik.am");
            helper.setSubject("Notification from Cleaning Recorder");
            helper.setCc(users.stream().map(CleaningUser::getEmail).toArray(String[]::new));
            helper.setText(templateEngine.process("mail", context), true);
        };

        messages.add(preparator);
    }

    @RequestMapping("/check")
    String forceCheck() {
        checkAll();
        return "OK";
    }

    @Scheduled(cron = "0 0 8/18/21 * * *", zone = "JST")
    void checkAll() {
        List<CleaningType> types = cleaningTypeRepository.findAll();
        LocalDate now = LocalDate.now();

        List<TypeNameAndLimit> limits = new ArrayList<>();
        for (CleaningType type : types) {
            LocalDate lastDate = cleaningEventRepository.findFirstByCleaningType_TypeIdOrderByEventDateDesc(type.getTypeId())
                    .map(e -> Date.class.cast(e.getEventDate()).toLocalDate())
                    .orElse(installedDate.asLocalDate());
            LocalDate limit = type.limitDate(lastDate);
            if (limit.isBefore(now)) {
                limits.add(new TypeNameAndLimit(type.getTypeName(), limit));
            }
        }
        if (!limits.isEmpty()) {
            reserveToSend(limits);
        }
    }

    @Scheduled(fixedRate = 60_000)
    void send() {
        while (!messages.isEmpty()) {
            MimeMessagePreparator preparator = messages.poll();
            log.info("send {}", preparator);
            mailSender.send(preparator);
        }
    }

    @Data
    public static class TypeNameAndLimit {
        private final String typeName;
        private final LocalDate limit;
    }
}
