package cdit_automation.enums;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public enum FileTypeEnum {
    HDB_PROPERTY("HDB_PROPERTY"),
    MSF_PWD("MSF_PWD"),
    MSF_TRANSITIONAL_SHELTER("MSF_TRANSITIONAL_SHELTER"),
    IRAS_FORM_B("IRAS_FORM_B"),
    IRAS_DECLARED_NTI("IRAS_DECLARED_NTI"),
    SINGPOST_SELF_EMPLOYED("SINGPOST_SELF_EMPLOYED"),
    MHA_BULK_CITIZEN("MHA_BULK_CITIZEN"),
    MHA_NEW_CITIZEN("MHA_NEW_CITIZEN"),
    MHA_NO_INTERACTION("MHA_NO_INTERACTION"),
    MHA_CHANGE_ADDRESS("MHA_CHANGE_ADDRESS"),
    MHA_DUAL_CITIZEN("MHA_DUAL_CITIZEN"),
    MHA_PERSON_DETAIL_CHANGE("MHA_PERSON_DETAIL_CHANGE"),
    MHA_DEATH_DATE("MHA_DEATH_DATE"),
    MHA_CEASED_CITIZEN("MHA_CEASED_CITIZEN");

    private String value;

    FileTypeEnum(String value) {
        this.value = value;
    }

    public static FileTypeEnum fromString(String name) {
        for (FileTypeEnum enumVal : FileTypeEnum.values()) {
            if (enumVal.getValue().equals(name)) {
                return enumVal;
            }
        }
        return null;
    }
}
