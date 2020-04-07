package cds_automation.enums.datasource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum NcaAddressTypeEnum {
    RESIDENTIAL("S");

    private String value;

    NcaAddressTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    // returns the relevant enum for a given string value
    public static NcaAddressTypeEnum fromString(String str) {
        for (NcaAddressTypeEnum enumVal : NcaAddressTypeEnum.values()) {
            if (enumVal.getValue().equals(str)) {
                return enumVal;
            }
        }
        return null;
    }

    public static List<String> asList() {
        List<String> stringList = new ArrayList<>();
        for(NcaAddressTypeEnum ncaAddressTypeEnum : NcaAddressTypeEnum.values()) {
            stringList.add(ncaAddressTypeEnum.getValue());
        }
        return stringList;
    }

    public static NcaAddressTypeEnum pick() {
        return NcaAddressTypeEnum.values()[new Random().nextInt(NcaAddressTypeEnum.values().length)];
    }

    public static String unsupportedValue() {
        return "W";
    }

    @Override
    public String toString() {
        return this.value;
    }
}
