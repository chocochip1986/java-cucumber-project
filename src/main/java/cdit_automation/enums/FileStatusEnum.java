package cdit_automation.enums;

public enum FileStatusEnum {
    ENCRYPTED_HAS_VIRUS("ENCRYPTED_HAS_VIRUS"),
    FAIL_TO_DECRYPT("FAIL_TO_DECRYPT"),
    FAIL_TO_HASH("FAIL_TO_HASH"),
    FAIL_TO_UNZIP("FAIL_TO_UNZIP"),
    DUPLICATED("DUPLICATED"),
    DECRYPTED_HAS_VIRUS("DECRYPTED_HAS_VIRUS"),
    OK("OK");

    private String value;

    FileStatusEnum(String value) {
        this.value = value;
    }
}
