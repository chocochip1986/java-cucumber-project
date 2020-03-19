package cdit_automation.enums;

import java.util.Random;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum PreparedPropertyTypeEnum {
    ONE_ROOM_HDB("ONE_ROOM_HDB"),
    TWO_ROOM_HDB("TWO_ROOM_HDB"),
    THREE_ROOM_HDB("THREE_ROOM_HDB"),
    FOUR_ROOM_HDB("FOUR_ROOM_HDB"),
    FIVE_ROOM_HDB("FIVE_ROOM_HDB"),
    HDB_MULTI_GENERATION_FLAT("HDB_MULTI_GENERATION_FLAT"),
    HDB_EXECUTIVE_FLAT("HDB_EXECUTIVE_FLAT"),
    SHOP_HOUSE_OWNED_BY_HDB_ONE_ROOM("SHOP_HOUSE_OWNED_BY_HDB_ONE_ROOM"),
    SHOP_HOUSE_OWNED_BY_HDB_TWO_ROOM("SHOP_HOUSE_OWNED_BY_HDB_TWO_ROOM"),
    SHOP_HOUSE_OWNED_BY_HDB_THREE_ROOM("SHOP_HOUSE_OWNED_BY_HDB_THREE_ROOM"),
    SHOP_HOUSE_OWNED_BY_HDB_FOUR_ROOM("SHOP_HOUSE_OWNED_BY_HDB_FOUR_ROOM"),
    SHOP_HOUSE_OWNED_BY_HDB_FIVE_ROOM("SHOP_HOUSE_OWNED_BY_HDB_FIVE_ROOM"),
    SHOP_HOUSE_OWNED_BY_HDB_MULTI_GENERATION_FLAT("SHOP_HOUSE_OWNED_BY_HDB_MULTI_GENERATION_FLAT"),
    SHOP_HOUSE_OWNED_BY_HDB_EXECUTIVE_FLAT("SHOP_HOUSE_OWNED_BY_HDB_EXECUTIVE_FLAT"),
    SHOP_HOUSE_NOT_OWNED_BY_HDB_ONE_ROOM("SHOP_HOUSE_NOT_OWNED_BY_HDB_ONE_ROOM"),
    SHOP_HOUSE_NOT_OWNED_BY_HDB_TWO_ROOM("SHOP_HOUSE_NOT_OWNED_BY_HDB_TWO_ROOM"),
    SHOP_HOUSE_NOT_OWNED_BY_HDB_THREE_ROOM("SHOP_HOUSE_NOT_OWNED_BY_HDB_THREE_ROOM"),
    SHOP_HOUSE_NOT_OWNED_BY_HDB_FOUR_ROOM("SHOP_HOUSE_NOT_OWNED_BY_HDB_FOUR_ROOM"),
    SHOP_HOUSE_NOT_OWNED_BY_HDB_FIVE_ROOM("SHOP_HOUSE_NOT_OWNED_BY_HDB_FIVE_ROOM"),
    SHOP_HOUSE_NOT_OWNED_BY_HDB_EXECUTIVE_FLAT("SHOP_HOUSE_NOT_OWNED_BY_HDB_EXECUTIVE_FLAT"),
    NON_SHOP_HOUSE("NON_SHOP_HOUSE"),
    TWO_ROOM_PRE_WAR_HDB("TWO_ROOM_PRE_WAR_HDB"),
    THREE_ROOM_PRE_WAR_HDB("THREE_ROOM_PRE_WAR_HDB"),
    FOUR_ROOM_PRE_WAR_HDB("FOUR_ROOM_PRE_WAR_HDB"),
    FIVE_ROOM_PRE_WAR_HDB("FIVE_ROOM_PRE_WAR_HDB"),
    PRE_WAR_HDB_EXECUTIVE_FLAT("PRE_WAR_HDB_EXECUTIVE_FLAT"),
    PRIVATE_PROPERTY("PRIVATE_PROPERTY"),
    NO_PROPERTY_TYPE("NO_PROPERTY_TYPE"),
    OVERSEAS("OVERSEAS");

    private String value;

    private PreparedPropertyTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    // returns the relevant enum for a given string value
    public static PreparedPropertyTypeEnum fromString(String str) {
        for (PreparedPropertyTypeEnum pt : PreparedPropertyTypeEnum.values()) {
            if (pt.getValue().equals(str)) {
                return pt;
            }
        }
        return null;
    }

    public static PreparedPropertyTypeEnum pick() {
        return PreparedPropertyTypeEnum.values()[new Random().nextInt(PreparedPropertyTypeEnum.values().length)];
    }
}
