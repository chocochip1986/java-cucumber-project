package cdit_automation.data_setup;

import com.github.javafaker.Faker;
import com.sun.org.apache.bcel.internal.generic.ARRAYLENGTH;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

@Slf4j
public class Phaker {
    private static final String[] NRIC_FIRST_CHAR = {"S", "T"};
    private static final Map<String, String> NRIC_MAP = createNricMap();
    private static Set<String> usedValidNrics = new HashSet<>();

    private static final String[] FIN_FIRST_CHAR = {"F", "G"};
    private static final Map<String, String> FIN_MAP = createFinMap();
    private static Set<String> usedValidFins = new HashSet<>();

    private static final Random rand = new Random();
    private static final Faker faker = new Faker();

    private static final String[] NUMBERS = new String[]{"1", "2", "3", "4", "5", "6", "7", "9", "0"};
    private static final String[] ALPHABETS = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    public static String postalCode() {
        return genRandomNumbers(6);
    }

    public static String streetName() {
        return faker.address().streetName();
    }

    public static String blockNo() {
        return genRandomNumbers(2) + genRandomALPHABETS(1);
    }

    public static String floorNo() {
        return genRandomNumbers(3);
    }

    public static String unitNo() {
        return genRandomNumbers(2);
    }

    public static String validNric() {
        int count = 0;
        int retryCount = 10000;
        String generatedNric = null;
        while (count < retryCount) {
            String nric = nric();
            if (!usedValidNrics.contains(nric)) {
                usedValidNrics.add(nric);
                generatedNric = nric;
                break;
            }
            count++;
        }
        if (generatedNric == null) {
            log.error("Unable to generate a unique valid nric!!!");
            return null;
        }
        return generatedNric;
    }

    public static String validFin() {
        int count = 0;
        int retryCount = 10;
        String generatedFin = null;
        while (count < retryCount) {
            String fin = fin();
            if (!usedValidFins.contains(fin)) {
                usedValidFins.add(fin);
                generatedFin = fin;
                break;
            }
            count++;
        }
        if (generatedFin == null) {
            log.error("Unable to generate a unique valid fin!!!");
            return null;
        }
        return generatedFin;
    }

    public static String nric() {
        String firstChar = randomItemFromArray(NRIC_FIRST_CHAR);
        String midNum = faker.number().digits(7);
        return firstChar + midNum + validChecksum(firstChar, midNum, NRIC_MAP);
    }

    public static String fin() {
        String firstChar = randomItemFromArray(FIN_FIRST_CHAR);
        String midNum = faker.number().digits(7);
        return firstChar + midNum + validChecksum(firstChar, midNum, FIN_MAP);
    }

    public static <E> E randomItemFromArray(E[] input) {
        return input[rand.nextInt(input.length)];
    }

    private static Map<String, String> createNricMap() {
        Map<String, String> map = new HashMap<>();
        map.put("0", "A");
        map.put("1", "B");
        map.put("2", "C");
        map.put("3", "D");
        map.put("4", "E");
        map.put("5", "F");
        map.put("6", "G");
        map.put("7", "H");
        map.put("8", "I");
        map.put("9", "Z");
        map.put("10", "J");

        return map;
    }

    private static Map<String, String> createFinMap() {
        Map<String, String> map = new HashMap<>();
        map.put("0", "K");
        map.put("1", "L");
        map.put("2", "M");
        map.put("3", "N");
        map.put("4", "P");
        map.put("5", "Q");
        map.put("6", "R");
        map.put("7", "T");
        map.put("8", "U");
        map.put("9", "W");
        map.put("10", "X");

        return map;
    }

    private static String validChecksum(String firstChar, String digits, Map<String, String> map) {
        int checksum =
                (2 * Character.getNumericValue(digits.charAt(0))
                        + 7 * Character.getNumericValue(digits.charAt(1))
                        + 6 * Character.getNumericValue(digits.charAt(2))
                        + 5 * Character.getNumericValue(digits.charAt(3))
                        + 4 * Character.getNumericValue(digits.charAt(4))
                        + 3 * Character.getNumericValue(digits.charAt(5))
                        + 2 * Character.getNumericValue(digits.charAt(6)));

        if ("TG".contains(firstChar)) {
            checksum += 4;
        }

        int remainder = 10 - checksum % 11;
        return map.get(String.valueOf(remainder));
    }

    private static String genRandomNumbers(int length) {
        if ( length < 1 ){
            return "";
        }
        String finalNumber = "";
        for ( int i = 0 ; i < length ; i++ ) {
            finalNumber+=genRandomNumber();
        }

        return finalNumber;
    }

    private static String genRandomALPHABETS(int length) {
        if ( length < 1 ) {
            return "";
        }

        String finalAlphabet = "";
        for ( int i = 0 ; i < length ; i++ ) {
            finalAlphabet+=genRandomAlphbet();
        }

        return finalAlphabet;
    }

    private static String genRandomAlphbet() {
        return ALPHABETS[new Random().nextInt(ALPHABETS.length)];
    }

    private static String genRandomNumber() {
        return NUMBERS[new Random().nextInt(NUMBERS.length)];
    }
}
