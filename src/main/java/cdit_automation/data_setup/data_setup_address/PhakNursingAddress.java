package cdit_automation.data_setup.data_setup_address;

import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.HomeTypeEnum;
import cdit_automation.enums.SpecialMappingEnum;

import java.util.Random;

public class PhakNursingAddress {
    private static final String STREET_NAMES[] = new String[]{
            "3007 Ubi Rd", "255 Bukit Timah Rd", "11 Jalan Ampas", "363 Balestier Rd", "49 Upper Thomson Rd", "31 Joo Chiat Lane"
    };

    private static final String POSTAL_CODE_SECTORS[] = new String[]{
            "40", "25", "32", "57", "42"
    };

    private static final String BUILDING_NAMES[] = new String[]{
            "Rainbow Care", "Good Shepherd Loft", "Irene Nursing Home", "Orange Valley Care Centre", "St. Theresa's Home", "Serene Nursing Home"
    };

    public static PhakAbstractAddress fakeItTillYouMakeIt() {
        PhakAbstractAddress phakAbstractAddress = new PhakAbstractAddress(recommendAUnitNo(), recommendABlockNo(), recommendAFloorNo(), recommendAStreetName(), recommendABuildingName(), recommendAnOldPostalCode(), recommendAPostalCode(), recommendAStreetCode(), PhakNursingAddress.class);
        phakAbstractAddress.setSpecialProperty(true);
        phakAbstractAddress.setSpecialMappingEnum(SpecialMappingEnum.NURSING_HOMES);
        phakAbstractAddress.setHomeTypeEnum(HomeTypeEnum.pick());
        return phakAbstractAddress;
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
