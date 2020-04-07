package cds_automation.enums.datasource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum InvalidAddressTagEnum {
    DELISTED("D"),
    DEMOLISHED("M"),
    FAIL_TO_REPORT("F"),
    GONE_AWAY("G"),
    INVALID_ADDRESS("I"),
    NO_SUCH_ADDRESS("N"),
    OUTDATED_ADDRESS("P"),
    OVERSEAS("S"),
    SPACE("");

    private String value;

    InvalidAddressTagEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    // returns the relevant enum for a given string value
    public static InvalidAddressTagEnum fromString(String str) {
        for (InvalidAddressTagEnum enumVal : InvalidAddressTagEnum.values()) {
            if (enumVal.getValue().equals(str)) {
                return enumVal;
            }
        }
        return null;
    }

    public static List<String> asList() {
        List<String> stringList = new ArrayList<>();
        for(InvalidAddressTagEnum invalidAddressTagEnum : InvalidAddressTagEnum.values()) {
            if ( invalidAddressTagEnum.equals(InvalidAddressTagEnum.SPACE) ) {
                stringList.add("\\s");
            } else {
                stringList.add(invalidAddressTagEnum.getValue());
            }
        }
        return stringList;
    }

    public static InvalidAddressTagEnum pick() {
        return InvalidAddressTagEnum.values()[new Random().nextInt(InvalidAddressTagEnum.values().length)];
    }

    public static String unsupportedValue() {
        return "W";
    }

    @Override
    public String toString() {
        return this.value.isEmpty() ? " " : this.value;
    }
}
