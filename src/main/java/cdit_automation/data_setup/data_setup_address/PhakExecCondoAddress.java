package cdit_automation.data_setup.data_setup_address;

import cdit_automation.data_setup.Phaker;
import java.util.Random;

public class PhakExecCondoAddress {
    private static final String STREET_NAMES[] = new String[]{
            "Yio Chu Kang Road", "Choa Chu Kang Avenue 5", "Woodlands Avenue 12", "Anchorvale Crescent", "Sembawang Road", "Choa Chu Kang Grove", "Sembawang Crescent", "Choa Chu Kang Avenue 3", "Yishun Street 51", "Canberra Drive", "Westwood Avenue",
            "Tampines Central 7"
    };

    private static final String POSTAL_CODE_SECTORS[] = new String[]{
            "01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
            "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
            "31", "32", "33", "34", "35", "36", "37", "38", "39", "40",
            "41", "42", "43", "44", "45", "46", "47", "48", "49", "50",
            "51", "52", "53", "54", "55", "56", "57", "58", "59", "60",
            "61", "62", "63", "64", "65", "66", "67", "68", "69", "70",
            "71", "72", "73", "75", "76", "77", "78", "79", "80"
    };

    private static final String BUILDING_NAMES[] = new String[]{
            "Hundred Palms Residences", "iNz Residence", "NorthWave", "Treasure Crest", "The Visionaire", "Sol Acres", "Parc Life", "Wandervale", "The Criterion",
            "The Brownstone", "Westwood Residences", "The Terrace", "Citylife@Tampines"
    };

    public static PhakAbstractAddress fakeItTillYouMakeIt() {
        return new PhakAbstractAddress(recommendAUnitNo(), recommendABlockNo(), recommendAFloorNo(), recommendAStreetName(), recommendABuildingName(), recommendAnOldPostalCode(), recommendAPostalCode(), recommendAStreetCode(), PhakExecCondoAddress.class);
    }

    private static String recommendAStreetCode() {
        return Phaker.validNumber(4);
    }

    private static String recommendAUnitNo() {
        return Phaker.validNumber(2);
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
