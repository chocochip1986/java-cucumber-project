package cdit_automation.data_helpers;

import cdit_automation.configuration.StepDefLevelTestContext;
import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.Gender;
import cdit_automation.exceptions.TestFailException;
import cdit_automation.utilities.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class MhaBulkFileDataPrep extends BatchFileDataPrep {

    private static final String DEFAULT_RESIDENTIAL_STATUS = "Singaporean";
    private static final String DEFAULT_ADDRESS = "MHAAddress";

    private static final String RANDOM_PEOPLE_OPTION = "RandomPeople";
    private static final String[] RESIDENTIAL_STATUS_OPTIONS = {"Singaporean", "PermanentResident", "Foreigner", "DualCitizen"};
    private static final String[] ADDRESS_OPTIONS = {"MHAAddress", "NCAAddress", "Overseas", "InvalidAddressInd"};
    private static final String[] PERSON_OPTIONS = {"Alive", "Dead",
            "Male", "Female", "Unknown", "FutureBD", "FutureDD", "DeathDateBeforeBirthDate", "InvalidNric", "InvalidFin", ""};
    private static final String[] MHA_ADDRESS_OPTIONS = {"MhaAddrType", "InvalidMhaAddrType", "BlkNo", "StrtName", "FlrNo", "BuildName", "PostalCode", "NewPostalCode", "InvalidAddressTag", "InvalidInvalidAddressTag"};
    private static final String[] NCA_ADDRESS_OPTIONS = {"NcaAddrType", "InvalidNcaAddrType", "BlkNo", "StrCode", "LvlNo", "UnitNo", "PostalCode", "NewPostalCode"};

    public List<String> createBodyOfTestScenarios(List<String> listOfEntries, StepDefLevelTestContext testContext) {
        List<String> body = new ArrayList<>();

        for ( int i = 0 ; i < listOfEntries.size() ; i++ ) {
            body.add(mhaBulkFileEntryCreator(listOfEntries.get(i)));
        }
        return body;
    }

    private String mhaBulkFileEntryCreator(String scenerioEntry) {
        if (scenerioEntry == null) {
            throw new TestFailException("There are no valid options to create an entry in the MHA Bulk File!");
        }
        List<String> optionsList = Arrays.stream(scenerioEntry.split(",")).collect(Collectors.toList());
        if (optionsList.size() == 0) {
            throw new TestFailException("There are no valid options to create an entry in the MHA Bulk File!");
        }

        return process(optionsList);
    }

    private String process(List<String> optionsList) {
        int quantity = getQuantity(optionsList);

        if ( hasRandomPeopleOption(optionsList) ) {
            //TODO create random people
            return "";
        }
        else {
            String residentialStatusOption = findResidentialStatusOption(optionsList);
            String addressOption = findAddressOptions(optionsList);

            return createPerson(optionsList);
        }
    }

    private String createPerson(List<String> optionsList) {
        String dob = createBirthDate(optionsList);
        return createNric(optionsList)+createFin(optionsList)+createName(optionsList)+dob+createDeathDate(optionsList, dob)+createGender(optionsList);
    }

    private String createFin(List<String> optionsList) {
        return optionsList
                .stream()
                .filter(option -> option.matches("^Fin:(.{9}|Invalid)$"))
                .findFirst()
                .map(option -> {
                    if ( option.substring(5).matches("Invalid") ) {
                        optionsList.remove(option);
                        return Phaker.invalidNric();
                    } else {
                        optionsList.remove(option);
                        return option;
                    }
                })
                .orElse("        ");
    }

    private String createNric(List<String> optionsList) {
        return optionsList
                .stream()
                .filter(option -> option.matches("^Nric:(.{9}|Invalid)$"))
                .findFirst().map(option -> {
                    if ( option.substring(5).matches("Invalid") ) {
                        optionsList.remove(option);
                        return Phaker.invalidNric();
                    } else {
                        optionsList.remove(option);
                        return option;
                    }
                })
                .orElse(Phaker.validNric());
    }

    private String createGender(List<String> optionsList) {
        return optionsList
                .stream()
                .filter(option -> option.matches("^(M|F|U|InvalidGender)$"))
                .findFirst()
                .map(option -> {
                    if ( option.equals("InvalidGender") ) {
                        optionsList.remove(option);
                        return "A";
                    } else {
                        Gender gender = Gender.fromString(option);
                        optionsList.remove(option);
                        return gender.getValue();
                    }
                })
                .orElse(Gender.MALE.getValue());
    }

    private String createDeathDate(List<String> optionsList, String dob) {
        LocalDate birthDate = LocalDate.parse(dob, Phaker.DATETIME_FORMATTER_YYYYMMDD);
        return optionsList
                .stream()
                .filter(option -> option.matches("^DoD(:([A-Za-z]+|[\\d]{8}))?$"))
                .findFirst().map(option -> {
                    try {
                        if ( option.length() == 4 ) {
                            optionsList.remove(option);
                            return Phaker.validDateFromRange(birthDate, dateUtils.now()).format(Phaker.DATETIME_FORMATTER_YYYYMMDD);
                        } else if ( option.substring(4).matches("^DeathBeforeBirth$") ) {
                            optionsList.remove(option);
                            return birthDate.minusDays(1L).format(Phaker.DATETIME_FORMATTER_YYYYMMDD);
                        } else if ( option.substring(4).matches("^[\\d]{8}$") ) {
                            optionsList.remove(option);
                            return LocalDate.parse(option.substring(4), Phaker.DATETIME_FORMATTER_YYYYMMDD).format(Phaker.DATETIME_FORMATTER_YYYYMMDD);
                        } else {
                            return null;
                        }
                    } catch ( DateTimeParseException | IndexOutOfBoundsException e ) {
                        return null;
                    }
                })
                .orElse("        ");
    }

    private String createBirthDate(List<String> optionsList) {
        return optionsList
                .stream()
                .filter(option -> option.matches("^DoB:[0-9]{8}$"))
                .findFirst().map(option -> {
                    try {
                        String date = LocalDate.parse(option.substring(4), Phaker.DATETIME_FORMATTER_YYYYMMDD).format(Phaker.DATETIME_FORMATTER_YYYYMMDD);
                        optionsList.remove(option);
                        return date;
                    }
                    catch (DateTimeParseException e) {
                        optionsList.remove(option);
                        return null;
                    }
                }).orElse(Phaker.validPastDate().format(Phaker.DATETIME_FORMATTER_YYYYMMDD));

    }

    private String createName(List<String> optionsList) {
        return optionsList
                .stream()
                .filter(option -> option.matches("^Name:.*$"))
                .findFirst().map(option -> {
                    if( option.length() <= 5 ) {
                        return StringUtils.leftPad(option.substring(5), 66);
                    } else {
                        return null;
                    }
                }).orElse(StringUtils.leftPad(Phaker.validName(), 66));
    }

    private String findAddressOptions(List<String> optionsList) {
        for ( int i = 0 ;  i < ADDRESS_OPTIONS.length ; i++ ) {
            if ( optionsList.contains(ADDRESS_OPTIONS[i]) ) {
                optionsList.remove(ADDRESS_OPTIONS[i]);
                return ADDRESS_OPTIONS[i];
            }
        }
        return DEFAULT_ADDRESS;
    }

    private String findResidentialStatusOption(List<String> optionsList) {
        for ( int i = 0 ; i < RESIDENTIAL_STATUS_OPTIONS.length ; i++ ) {
            if ( optionsList.contains(RESIDENTIAL_STATUS_OPTIONS[i]) ) {
                optionsList.remove(RESIDENTIAL_STATUS_OPTIONS[i]);
                return RESIDENTIAL_STATUS_OPTIONS[i];
            }
        }
        return DEFAULT_RESIDENTIAL_STATUS;
    }

    private boolean hasRandomPeopleOption(List<String> optionsList) {
        if ( optionsList.contains(RANDOM_PEOPLE_OPTION)) {
            return true;
        }
        else {
            return false;
        }
    }

    private int getQuantity(List<String> optionsList) {
        String quantity = optionsList.stream().filter(option -> option.matches("^Quantity:[1-9][0-9]*$")).findFirst().orElse("Quantity:1");
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher match = pattern.matcher(quantity);
        if ( match.find() ) {
            return Integer.valueOf(match.group());
        }
        else {
            return 1;
        }
    }
}
