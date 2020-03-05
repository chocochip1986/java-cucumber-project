package cdit_automation.data_setup.data_setup_address;

import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.PropertyTypeEnum;

import java.util.Random;

public class PhakIslandAddress {
    private static final String STREET_NAMES[] = new String[]{
            "Jalan Noordin", "Jalan Batu Ubin", "Jalan Sam Heng", "Jalan Jelutong", "224J Jalan Endut Senin"};

    private static final String BUILDING_NAMES[] = new String[]{
            "Wei Tuo Fa Gong Temple",
            "Na Du Gong Shrine",
            "Ubin Living Lab"
    };

    private static final String POSTAL_CODE_SECTORS[] = new String[]{
            "50"
    };

    public static PhakAbstractAddress fakeItTillYouMakeIt() {
        return new PhakAbstractAddress(null, null, null, recommendAStreetName(), recommendABuildingName(), null, recommendAPostalCode(), PhakIslandAddress.class);
    }

    protected static String recommendAStreetName() {
        return STREET_NAMES[new Random().nextInt(STREET_NAMES.length)];
    }

    protected static String recommendABuildingName() {
        return BUILDING_NAMES[new Random().nextInt(BUILDING_NAMES.length)];
    }

    protected static String recommendAPostalCode() {
        return recommandAPostalCodeSector()+Phaker.validNumber(4);
    }

    protected static String recommandAPostalCodeSector() {
        return POSTAL_CODE_SECTORS[new Random().nextInt(POSTAL_CODE_SECTORS.length)];
    }
}
