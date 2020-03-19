package cdit_automation.enums;

import java.util.Random;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public enum FileTypeEnum {
    MHA_BULK_CITIZEN("MHA_BULK_CITIZEN"),
    MHA_NEW_CITIZEN("MHA_NEW_CITIZEN"),
    MHA_NO_INTERACTION("MHA_NO_INTERACTION"),
    MHA_CHANGE_ADDRESS("MHA_CHANGE_ADDRESS"),
    MHA_DUAL_CITIZEN("MHA_DUAL_CITIZEN"),
    MHA_PERSON_DETAIL_CHANGE("MHA_PERSON_DETAIL_CHANGE"),
    MHA_DEATH_DATE("MHA_DEATH_DATE"),
    MHA_CEASED_CITIZEN("MHA_CEASED_CITIZEN"),

    IRAS_FORM_B("IRAS_FORM_B"),
    IRAS_DECLARED_NTI("IRAS_DECLARED_NTI"),
    IRAS_BULK_AI("IRAS_BULK_AI"),
    IRAS_THRICE_MONTHLY_AI("IRAS_THRICE_MONTHLY_AI"),

    HDB_PROPERTY("HDB_PROPERTY"),
    MSF_PWD("MSF_PWD"),
    MSF_TRANSITIONAL_SHELTER("MSF_TRANSITIONAL_SHELTER"),
    SINGPOST_SELF_EMPLOYED("SINGPOST_SELF_EMPLOYED"),

    CPFB_CLASSIFIED_ACCOUNT("CPFB_CLASSIFIED_ACCOUNT"),
    CPFB_LORONG_BUANGKOK("CPFB_LORONG_BUANGKOK"),
    CPFB_NURSING_HOME("CPFB_NURSING_HOME");

    private String value;
    private String humanized_value;

    FileTypeEnum(String value) {
        this.value = value;
        this.humanized_value = value.replace("_", " ");
    }

    public static FileTypeEnum fromString(String name) {
        for (FileTypeEnum enumVal : FileTypeEnum.values()) {
            if (enumVal.getValue().equals(name)) {
                return enumVal;
            }
        }
        return null;
    }

    public static FileTypeEnum randomValidFileTypeEnum() {
        return FileTypeEnum.values()[new Random().nextInt(FileTypeEnum.values().length)];
    }
}
