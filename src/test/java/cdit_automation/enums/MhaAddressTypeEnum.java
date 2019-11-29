package cdit_automation.enums;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum MhaAddressTypeEnum {
    APARTMENT_BLK("A"),
    WITHOUT_APARTMENT_BLK("B"),
    REVERSE_OF_APARTMENT_BLK_STREET_NAME("X"),
    OVERSEAS_ADDRESS("C"),
    PRIVATE_FLATS_WITH_APARTMENT_BLK("D"),
    CO_APARTMENT_BLK("E"),
    CO_WITHOUT_APARTMENT_BLK("F"),
    QUARTER_ADDRESS("Q"),
    ISLAND_ADDRESS("I"),
    SPACE("");

    private String value;

    MhaAddressTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    // returns the relevant enum for a given string value
    public static MhaAddressTypeEnum fromString(String str) {
        for (MhaAddressTypeEnum enumVal : MhaAddressTypeEnum.values()) {
            if (enumVal.getValue().equals(str)) {
                return enumVal;
            }
        }
        return null;
    }

    public static List<String> asList() {
        List<String> stringList = new ArrayList<>();
        for(MhaAddressTypeEnum addrTypeEnum : MhaAddressTypeEnum.values()) {
            stringList.add(addrTypeEnum.getValue());
        }
        return stringList;
    }

    public static MhaAddressTypeEnum pick() {
        return MhaAddressTypeEnum.values()[new Random().nextInt(MhaAddressTypeEnum.values().length)];
    }

    public static String unsupportedValue() {
        return "W";
    }

    @Override
    public String toString() {
        return this.value;
    }
}
