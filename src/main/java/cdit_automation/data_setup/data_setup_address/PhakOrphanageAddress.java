package cdit_automation.data_setup.data_setup_address;

import cdit_automation.data_setup.Phaker;

import java.util.Random;

public class PhakOrphanageAddress {
    private static final String STREET_NAMES[] = new String[]{
            "5 Mattar Road", "Toa Payoh Lorong 1", "381 Lorong 1 Toa Payoh", "350 Pasir Panjang Rd"
    };

    private static final String POSTAL_CODE_SECTORS[] = new String[]{
            "31", "38", "46", "34", "31", "11"
    };

    private static final String BUILDING_NAMES[] = new String[]{
            "Darul Ihsan Boys' Orphanage", "Singapore Children's Society", "Youth Service Centre", "Roundbox @ Children's Society", "The Salvation Army"
    };

    public static PhakAbstractAddress fakeItTillYouMakeIt() {
        return new PhakAbstractAddress(recommendAUnitNo(), recommendABlockNo(), recommendAFloorNo(), recommendAStreetName(), recommendABuildingName(), null, recommendAPostalCode(), PhakOrphanageAddress.class);
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
}
