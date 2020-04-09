package automation.data_setup;

import automation.enums.datasource.GenderEnum;
import com.github.javafaker.Faker;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

@Slf4j
public class Phaker {

    public static final String SG = "SG";
    
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
    private static final GenderEnum[] GENDERS = GenderEnum.values();
    private static final String[] MOBILENUMBER = new String[]{"9", "8"};

    private static final int[] DAYS_IN_MONTHS = new int[]{30, 27, 30, 29, 30, 29, 30, 30, 29, 30, 29, 30};
    public static final DateTimeFormatter DATETIME_FORMATTER_YYYYMMDD =
            DateTimeFormatter.ofPattern("yyyyMMdd");

    public static final DateTimeFormatter DATETIME_FORMATTER_YYYYMMDD_HHMMSS =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final LocalDate defaultLowerBoundaryDate = LocalDate.of(LocalDate.now().getYear() - 200, 1, 1);
    public static final LocalDate defaultUpperBoundaryDate = LocalDate.of(LocalDate.now().getYear() + 200, 12, 31);

    public static final int[] rangeOfLeapYears = calculateRangeOfLeapYears();

    public static final String[] POSTAL_SECTOR = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
            "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
            "31", "32", "33", "34", "35", "36", "37", "38", "39", "40",
            "41", "42", "43", "44", "45", "46", "47", "48", "49", "50",
            "51", "52", "53", "54", "55", "56", "57", "58", "59", "60",
            "61", "62", "63", "64", "65", "66", "67", "68", "69", "70",
            "71", "72", "73", "75", "76", "77", "78", "79", "80"};

    private static int orderedNricCount = 0;
    private static int orderedFinCount = 0;

    public static String fakeMd5() {
        return genRandomALPHABETS(64);
    }

    public static LocalDate validLeapDay() {
        int indexOfYearElement = 0 + (int)Math.round(Math.random() * rangeOfLeapYears.length-1);

        return LocalDate.of(rangeOfLeapYears[indexOfYearElement], 2, 29);
    }

    public static LocalDate validDateFromRange(LocalDate lowerBoundaryDate, LocalDate upperBoundaryDate) {
        //Inclusive of boundaries
        long days = lowerBoundaryDate.until(upperBoundaryDate.plusDays(1L), ChronoUnit.DAYS);
        return lowerBoundaryDate.plusDays(Math.round(Math.random() * days));
    }

    public static LocalDate validPastDate() {
        return validDate(defaultLowerBoundaryDate, LocalDate.now());
    }

    public static LocalDate validFutureDate() {

        return validDate(LocalDate.now(), defaultUpperBoundaryDate );
    }

    public static LocalDate validDate() {
        return validDate(null,null) ;
    }

    public static LocalDate validDate(LocalDate lowerBoundaryDate, LocalDate upperBoundaryDate) {
        int year = randomYear(lowerBoundaryDate == null ? defaultLowerBoundaryDate.getYear() : lowerBoundaryDate.getYear(), upperBoundaryDate == null ? defaultUpperBoundaryDate.getYear() : upperBoundaryDate.getYear());
        int month = randomMonth();
        int day = randomDayOfMonth(year, month);

        LocalDate date = LocalDate.of(year, month+1, day);
        return date;
    }

    public static String validEmail() {
        return faker.name().toString() + "@.com";
    }

    public static String validPSTNPhoneNumber() {
        return "6" + genRandomNumbers(7);
    }

    public static String validMobilePhoneNumber() {
        return genMobileNumber() + genRandomNumbers(8);
    }

    public static String validName() {
        return faker.name().lastName() + " " + faker.name().firstName();
    }

    public static GenderEnum validGender() {
        return GENDERS[new Random().nextInt(GENDERS.length)];
    }

    public static String validOldPostalCode() {
        return POSTAL_SECTOR[new Random().nextInt(POSTAL_SECTOR.length)]+genRandomNumbers(2);
    }

    public static String validPostalCode() {
        return POSTAL_SECTOR[new Random().nextInt(POSTAL_SECTOR.length)]+genRandomNumbers(4);
    }

    public static String validStreetName() {
        return faker.address().streetName();
    }

    public static String validBlockNo() {
        return genRandomNumbers(2) + genRandomALPHABETS(1);
    }

    public static String validBuildingName() {
        return Faker.instance().company().name();
    }

    public static String validFloorNo() {
        return genRandomNumbers(2);
    }

    public static String validUnitNo() {
        return genRandomNumbers(2);
    }

    public static String validNumber(@Positive int size) {
        return size < 1 ? genRandomNumbers(1) : genRandomNumbers(size);
    }

    public static String randomCountryCode() {
        return genRandomALPHABETS(2);
    }

    public static String randomNonSGCountryCode() {
        String countryCode;
        do {
            countryCode = randomCountryCode();
            if ( !countryCode.equals(SG)) {
                break;
            }
        } while (true);

        return countryCode;
    }

    public static String validNric() {
        int count = 0;
        int retryCount = 10000;
        String generatedNric = null;
        while (count < retryCount) {
            String nric = nric();
            if (!usedValidNrics.contains(nric) && !isABlackListedNric(nric)) {
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

    public static String invalidNric() {
        String validNric = nric();
        char checksum = validNric.toCharArray()[validNric.length()-1];
        return validNric.replace(checksum, (char)((checksum+1 - 'A') % 26 + 'A'));
    }

    public static String obtainValidNricChecksum(String firstChar, String digits) {
        return validChecksum(firstChar, digits, NRIC_MAP);
    }

    public static String obtainValidFinChecksum(String firstChar, String digits) {
        return validChecksum(firstChar, digits, FIN_MAP);
    }

    public static <E> E randomItemFromArray(E[] input) {
        return input[rand.nextInt(input.length)];
    }

    private static boolean isABlackListedNric(String nric) {
        Pattern blacklistedNricPatternS555 = Pattern.compile("^S555[0-9]{4}[A-Z]");
        Pattern blacklistedNricPatternS888 = Pattern.compile("^S888[0-9]{4}[A-Z]");

        return blacklistedNricPatternS555.matcher(nric).find() || blacklistedNricPatternS888.matcher(nric).find();
    }

    private static String nric() {
        String firstChar = randomItemFromArray(NRIC_FIRST_CHAR);
        String midNum = faker.number().digits(7);
        return firstChar + midNum + validChecksum(firstChar, midNum, NRIC_MAP);
    }

    private static String fin() {
        String firstChar = randomItemFromArray(FIN_FIRST_CHAR);
        String midNum = faker.number().digits(7);
        return firstChar + midNum + validChecksum(firstChar, midNum, FIN_MAP);
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

    public static String randomLetter() {
        return (faker.random().nextInt(26) + 'A') + "";
    }

    private static String genMobileNumber() {
        return MOBILENUMBER[new Random().nextInt(MOBILENUMBER.length)];
    }

    private static String genRandomAlphbet() {
        return ALPHABETS[new Random().nextInt(ALPHABETS.length)];
    }

    private static String genRandomNumber() {
        return NUMBERS[new Random().nextInt(NUMBERS.length)];
    }

    private static boolean isLeapYear(int year) {
        if ( year % 4 == 0 ) {
            if ( year % 100 == 0 ) {
                if ( year % 400 == 0 ) {
                    return true;
                }
                else {
                    return false;
                }
            }
            else {
                return true;
            }
        } else
        {
            return false;
        }
    }

    private static int[] calculateRangeOfLeapYears() {
        List<Integer> range = new ArrayList<>();
        for ( int i = defaultLowerBoundaryDate.getYear() ; i <= defaultUpperBoundaryDate.getYear() ; i++ ) {
            if ( isLeapYear(i) ) {
                range.add(i);
            }
        }
        return range.stream().mapToInt(i->i).toArray();
    }

    private static int randomYear(int lowerBoundaryYear, int upperBoundaryYear) {
        if ( lowerBoundaryYear < defaultLowerBoundaryDate.getYear() || lowerBoundaryYear > defaultUpperBoundaryDate.getYear() ) {
            lowerBoundaryYear = defaultLowerBoundaryDate.getYear();
        }
        if ( upperBoundaryYear > defaultUpperBoundaryDate.getYear() || upperBoundaryYear < defaultLowerBoundaryDate.getYear() ) {
            upperBoundaryYear = defaultUpperBoundaryDate.getYear();
        }
        int year = lowerBoundaryYear + (int)Math.round(Math.random() * (upperBoundaryYear-lowerBoundaryYear));

        return year;
    }

    private static int randomMonth() {
        return 0 + (int)Math.round(Math.random() * 11);
    }

    private static int randomDayOfMonth(int year, int month) {
        int day = 1;
        if ( isLeapYear(year) && month == 1 ) {
            //If month is Feb
            day = day + (int)Math.round(Math.random() * 28);
        }
        else {
            day = day + (int)Math.round(Math.random() * DAYS_IN_MONTHS[month]);
        }
        return day;
    }

    private static List<Month> generateListOfMonthsBetweenDates(LocalDate fromDate, LocalDate toDate) {

        return null;
    }

    public static String uniqueOrderFin() {
        final String firstLetter = orderedFinCount % 2 == 0 ? "F" : "G";
        final String digits = Strings.padStart(orderedFinCount+"", 7, '0');
        orderedFinCount +=1;
        return firstLetter + digits + validChecksum(firstLetter, digits, FIN_MAP);
    }

    public static String uniqueOrderNric() {
        orderedNricCount +=1;
        final String firstLetter = orderedNricCount % 2 == 0 ? "S" : "T";
        final String digits = Strings.padStart(orderedFinCount+"", 7, '0');
        orderedNricCount +=1;
        return firstLetter + digits + validChecksum(firstLetter, digits, NRIC_MAP);
    }
}
