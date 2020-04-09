package automation.enums.automation;

import java.util.Random;

public enum PropertyTypeEnum {
    LANDED("landed"),
    CONDO("condo"),
    HDB("hdb"),
    EXECUTIVE_CONDO("executive_condo"),
    CHALET("chalet"),
    KAMPONG("kampong"),
    ISLAND("island"),
    HOSPITAL("hospital"),
    INDUSTRIAL("industrial"),
    PRISON("prison"),
    ORPHANAGE("orphanage"),
    NURSING("nursing"),
    BUSINESS("business"),
    LORONG_BUANGKOK("lorong_buangkok"),
    EDUCATION("education");

    private String name;

    PropertyTypeEnum(String name) {
        this.name = name;
    }

    public static PropertyTypeEnum pick() {
        return PropertyTypeEnum.values()[new Random().nextInt(PropertyTypeEnum.values().length)];
    }

    public static PropertyTypeEnum fromString(String name) {
        for( PropertyTypeEnum propertyTypeEnum : PropertyTypeEnum.values() ) {
            if ( propertyTypeEnum.name.equals(name) ) {
                return propertyTypeEnum;
            }
        }
        return null;
    }
}
