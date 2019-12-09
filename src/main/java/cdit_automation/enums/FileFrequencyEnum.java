package cdit_automation.enums;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public enum FileFrequencyEnum {
    DAILY("DAILY"),
    WEEKLY("WEEKLY"),
    MONTHLY("MONTHLY"),
    YEARLY("YEARLY");

    private String value;

    FileFrequencyEnum(String value) {
        this.value = value;
    }
}
