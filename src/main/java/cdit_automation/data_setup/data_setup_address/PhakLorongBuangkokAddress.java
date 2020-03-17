package cdit_automation.data_setup.data_setup_address;

import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.SpecialMappingEnum;

import java.util.Random;

public class PhakLorongBuangkokAddress {
    private static final String STREET_NAMES[] = new String[]{
            "ISLAND", "PULAU", "P.", "SERAYA"};

    private static final String BUILDING_NAMES[] = new String[]{
            "Wei Tuo Fa Gong Temple",
            "Na Du Gong Shrine",
            "Ubin Living Lab"
    };

    private static final String POSTAL_CODES[] = new String[]{
            "547556", "547557", "547559", "547561", "547562", "547563", "547564", "547565",
            "547569", "547570", "547571", "547573", "547574", "547575", "547577", "547578",
            "547709", "547580", "547581", "547582", "547583", "547587", "547588", "547593",
            "547596", "547597"
    };

    public static PhakAbstractAddress fakeItTillYouMakeIt() {
        PhakAbstractAddress phakLorongBuangkokAddress = new PhakAbstractAddress(null, null, null, recommendAStreetName(), recommendABuildingName(), null, recommendAPostalCode(), PhakLorongBuangkokAddress.class);
        phakLorongBuangkokAddress.setSpecialProperty(true);
        phakLorongBuangkokAddress.setSpecialMappingEnum(SpecialMappingEnum.LORONG_BUANGKOK);
        return phakLorongBuangkokAddress;
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
        return POSTAL_CODES[new Random().nextInt(POSTAL_CODES.length)];
    }
}