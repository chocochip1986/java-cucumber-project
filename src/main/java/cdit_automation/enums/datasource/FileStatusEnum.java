package cdit_automation.enums.datasource;

import java.util.Random;

public enum FileStatusEnum {
    REJECTED("REJECTED"),
    OK("OK");

    private String value;

    FileStatusEnum(String value) {
        this.value = value;
    }

    public static FileStatusEnum randomValidFileStatusEnum() {
        return FileStatusEnum.values()[new Random().nextInt(FileStatusEnum.values().length)];
    }
}
