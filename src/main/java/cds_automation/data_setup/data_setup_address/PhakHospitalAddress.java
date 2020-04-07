package cds_automation.data_setup.data_setup_address;

import cds_automation.data_setup.Phaker;
import java.util.Random;

public class PhakHospitalAddress {
    private static final String STREET_NAMES[] = new String[]{
            "11 Jalan Tan Tock Seng", "71 Irrawaddy Road", "38 Irrawaddy Road", "100 Bukit Timah Road", "6A Napier Road"
    };

    private static final String POSTAL_CODE_SECTORS[] = new String[]{
            "30", "32", "22", "25"
    };

    private static final String BUILDING_NAMES[] = new String[]{
            "Tan Tock Seng Hospital", "Ren Ci Community Hospital", "Mt Elizabeth Novena Hospital", "KK Women's & Childn's Hospital", "Gleneagles Hospital"
    };

    public static PhakAbstractAddress fakeItTillYouMakeIt() {
        return new PhakAbstractAddress(recommendAUnitNo(), recommendABlockNo(), recommendAFloorNo(), recommendAStreetName(), recommendABuildingName(), recommendAnOldPostalCode(), recommendAPostalCode(), recommendAStreetCode(), PhakHospitalAddress.class);
    }

    private static String recommendAStreetCode() {
        return Phaker.validNumber(4);
    }

    private static String recommendAUnitNo() {
        return null;
    }

    private static String recommendABlockNo() {
        return null;
    }

    private static String recommendAFloorNo() {
        return null;
    }

    private static String recommendAStreetName() {
        return STREET_NAMES[new Random().nextInt(STREET_NAMES.length)];
    }

    private static String recommendABuildingName() {
        return BUILDING_NAMES[new Random().nextInt(BUILDING_NAMES.length)];
    }

    private static String recommendAnOldPostalCode() {
        return Phaker.validNumber(4);
    }

    private static String recommendAPostalCode() {
        return recommandAPostalCodeSector()+ Phaker.validNumber(4);
    }

    private static String recommandAPostalCodeSector() {
        return POSTAL_CODE_SECTORS[new Random().nextInt(POSTAL_CODE_SECTORS.length)];
    }
}
