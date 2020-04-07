package cds_automation.data_setup.data_setup_address;

import cds_automation.data_setup.Phaker;
import java.util.Random;

public class PhakKampongAddress {
    private static final String STREET_NAME = "Lorong Buangkok";

    private static final String[] POSTAL_CODES = new String[] {
            "547556", "547557", "547559", "547561", "547562", "547563", "547564", "547565", "547569", "547570", "547571", "547573", "547574",
            "547575", "547577", "547578", "547709", "547580", "547581", "547582", "547583", "547587", "547588", "547593", "547596", "547597"
    };

    public static PhakAbstractAddress fakeItTillYouMakeIt() {
        PhakAbstractAddress phakAbstractAddress = new PhakAbstractAddress(recommendAUnitNo(), recommendABlockNo(), recommendAFloorNo(), recommendAStreetName(), recommendABuildingName(), recommendAnOldPostalCode(), recommendAPostalCode(), recommendAStreetCode(), PhakKampongAddress.class);
        phakAbstractAddress.setSpecialProperty(true);
        return phakAbstractAddress;
    }

    private static String recommendAStreetCode() {
        return Phaker.validNumber(4);
    }

    private static String recommendAUnitNo() {
        return Phaker.validUnitNo();
    }

    private static String recommendABlockNo() {
        return null;
    }

    private static String recommendAFloorNo() {
        return null;
    }

    private static String recommendAStreetName() {
        return STREET_NAME;
    }

    private static String recommendABuildingName() {
        return null;
    }

    private static String recommendAPostalCode() {
        return POSTAL_CODES[new Random().nextInt(POSTAL_CODES.length)];
    }

    private static String recommendAnOldPostalCode() {
        return Phaker.validNumber(4);
    }
}
