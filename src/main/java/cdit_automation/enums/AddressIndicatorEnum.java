package cdit_automation.enums;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum AddressIndicatorEnum {
    NCA(" "),
    MHA_Z("Z"),
    MHA_C("C");

    private String value;

    AddressIndicatorEnum(String value){
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static AddressIndicatorEnum fromString(String str) {
        for(AddressIndicatorEnum addressIndicatorEnum : AddressIndicatorEnum.values()) {
            if(addressIndicatorEnum.getValue().equals(str)) {
                return addressIndicatorEnum;
            }
        }
        return null;
    }

    public static List<String> asList() {
        List<String> stringList = new ArrayList<>();
        for(AddressIndicatorEnum addressIndicatorEnum : AddressIndicatorEnum.values()) {
            stringList.add(addressIndicatorEnum.getValue());
        }
        return stringList;
    }

    public static String unsupportedValue() {
        return "A";
    }

    public static AddressIndicatorEnum pick() {
        return AddressIndicatorEnum.values()[new Random().nextInt(AddressIndicatorEnum.values().length)];
    }
}
