package cleaning.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum CleaningEventStatus {
    DONE("icon icon-check"), SKIP("icon icon-close");
    @Getter
    private final String styleClass;
}
