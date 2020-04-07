package cds_automation.enums.automation;

import java.util.Random;

public enum ResidencyEnum {
    RESIDENCE("RESIDENCE"),
    OWNERSHIP("OWNERSHIP"),
    BOTH("BOTH");

    private String name;
    ResidencyEnum (String name) {
        this.name = name;
    }

    public static ResidencyEnum pick() {
        return ResidencyEnum.values()[new Random().nextInt(ResidencyEnum.values().length)];
    }
}
