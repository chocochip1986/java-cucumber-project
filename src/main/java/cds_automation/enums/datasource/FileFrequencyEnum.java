package cds_automation.enums.datasource;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public enum FileFrequencyEnum {
    DAILY("DAILY"),
    WEEKLY("WEEKLY"),
    MONTHLY("MONTHLY"),
    THRICE_MONTHLY("THRICE_MONTHLY"),
    YEARLY("YEARLY");

    private String value;

    FileFrequencyEnum(String value) {
        this.value = value;
    }
}
