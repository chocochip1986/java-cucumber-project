package cdit_automation.data_setup.data_setup_address;

import cdit_automation.data_setup.Phaker;
import java.util.Random;

public class PhakPrisonAddress {
    private static final String STREET_NAMES[] = new String[]{
            "982 Upper Changi Rd North", "10 Tanah Merah Besar Rd"
    };

    private static final String POSTAL_CODE_SECTORS[] = new String[]{
            "50", "49"
    };

    private static final String BUILDING_NAMES[] = new String[]{
            "Changi Prison", "Tanah Merah Prison"
    };

    public static PhakAbstractAddress fakeItTillYouMakeIt() {
        return new PhakAbstractAddress(recommendAUnitNo(), recommendABlockNo(), recommendAFloorNo(), recommendAStreetName(), recommendABuildingName(), recommendAnOldPostalCode(), recommendAPostalCode(), recommendAStreetCode(), PhakPrisonAddress.class);
    }

    private static String recommendAStreetCode() {
        return Phaker.validNumber(4);
    }

    private static String recommendAUnitNo() {
        return Phaker.validUnitNo();
    }

    private static String recommendABlockNo() {
        return Phaker.validBlockNo();
    }

    private static String recommendAFloorNo() {
        return Phaker.validFloorNo();
    }

    private static String recommendAStreetName() {
        return STREET_NAMES[new Random().nextInt(STREET_NAMES.length)];
    }

    private static String recommendABuildingName() {
        return BUILDING_NAMES[new Random().nextInt(BUILDING_NAMES.length)];
    }

    private static String recommendAPostalCode() {
        return recommandAPostalCodeSector()+ Phaker.validNumber(4);
    }

    private static String recommandAPostalCodeSector() {
        return POSTAL_CODE_SECTORS[new Random().nextInt(POSTAL_CODE_SECTORS.length)];
    }

    private static String recommendAnOldPostalCode() {
        return Phaker.validNumber(4);
    }
}
