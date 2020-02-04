package cdit_automation.data_helpers;

import cdit_automation.constants.TestConstants;
import cdit_automation.data_helpers.batch_entities.MhaChangePersonDetailsFileEntry;
import cdit_automation.data_helpers.batch_entities.MhaChangePersonDetailsFileEntryCatBirthDate;
import cdit_automation.data_helpers.batch_entities.MhaChangePersonDetailsFileEntryCatGender;
import cdit_automation.data_helpers.batch_entities.MhaChangePersonDetailsFileEntryCatName;
import cdit_automation.data_helpers.batch_entities.MhaChangePersonDetailsFileEntryCatUnsupported;
import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.Gender;
import cdit_automation.enums.PersonDetailDataItemChangedEnum;
import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.PersonId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class MhaChangePersonDetailsDataPrep extends BatchFileDataPrep {
    private @Autowired AutowireCapableBeanFactory beanFactory;

    public List<String> createBodyOfTestScenarios(List<Map<String, String>> listOfScenarios) {
        List<String> body = new ArrayList<>();

        for (Map<String, String> scenario : listOfScenarios) {
            body.add(createScenarioEntry(scenario));
        }
        return body;
    }

    public String createScenarioEntry(Map<String, String> scenario) {
        String inputLine = setupDataForScenario(scenario.get("data_item_change_category").charAt(0), scenario);
        batchFileDataWriter.chunkOrWrite(inputLine);
        return inputLine;
    }

    private String setupDataForScenario(char itemChangeCat, Map<String, String> scenario) {
        MhaChangePersonDetailsFileEntry mhaChangePersonDetailsFileEntry;
        LocalDate dataItemChangeDate = createItemChangeDate(scenario.get("data_item_change_date"));
        LocalDate birthDate = generateBirthDate(dataItemChangeDate);
        String nric = nricFieldOptions(scenario.get("nric"));
        PersonId personId;
        switch (itemChangeCat) {
            case 'S':
                Gender originalGender = Gender.fromString(scenario.get("data_item_orig_value"));
                personId = personFactory.createNewSCPersonId(birthDate, null, null, originalGender, nric);
                mhaChangePersonDetailsFileEntry = new MhaChangePersonDetailsFileEntryCatGender(
                        nric,
                        scenario.get("data_item_change_value"),
                        dataItemChangeDate.format(dateUtils.DATETIME_FORMATTER_YYYYMMDD),
                        scenario.get("data_item_change_category").charAt(0), personId );
                break;
            case 'N':
                personId = personFactory.createNewSCPersonId(birthDate, null, scenario.get("data_item_orig_value"), null, nric);
                mhaChangePersonDetailsFileEntry = new MhaChangePersonDetailsFileEntryCatName(
                        nric,
                        scenario.get("data_item_change_value"),
                        dataItemChangeDate.format(dateUtils.DATETIME_FORMATTER_YYYYMMDD),
                        scenario.get("data_item_change_category").charAt(0), personId);
                break;
            case 'B':
                LocalDate origBirthDate = dateUtils.parse(scenario.get("data_item_orig_value"));
                personId = personFactory.createNewSCPersonId(origBirthDate, null, null, null, nric);
                mhaChangePersonDetailsFileEntry = new MhaChangePersonDetailsFileEntryCatBirthDate(
                        nric,
                        scenario.get("data_item_change_value"),
                        dataItemChangeDate.format(dateUtils.DATETIME_FORMATTER_YYYYMMDD),
                        scenario.get("data_item_change_category").charAt(0), personId);
                break;
            case 'D':
                //NOT GOING TO SUPPORT DEATH DATE CHANGES
                throw new TestFailException("Death Category will not supported!");
            default:
                mhaChangePersonDetailsFileEntry = new MhaChangePersonDetailsFileEntryCatUnsupported(nric,
                        scenario.get("data_item_change_value"),
                        dataItemChangeDate.format(dateUtils.DATETIME_FORMATTER_YYYYMMDD),
                        scenario.get("data_item_change_category").charAt(0), null);
                log.info("Unsupported item change category: "+itemChangeCat);
        }
        beanFactory.autowireBean(mhaChangePersonDetailsFileEntry);
        if ( !mhaChangePersonDetailsFileEntry.isValid() ) {
            String errorMsg = "Error with Mha Change Person Details Data Entry: "+mhaChangePersonDetailsFileEntry.getNric();
            errorMsg += System.lineSeparator()+String.join(System.lineSeparator(), mhaChangePersonDetailsFileEntry.getErrorMessages());
            log.warn(errorMsg);
        }
        return mhaChangePersonDetailsFileEntry.toString();
    }

    private LocalDate generateBirthDate(LocalDate upperBound) {
        LocalDate upperBoundaryDate = upperBound;
        if ( upperBound == null || upperBound.isAfter(TestConstants.DEFAULT_CUTOFF_DATE) ) {
            upperBoundaryDate = TestConstants.DEFAULT_CUTOFF_DATE;
        }
        return Phaker.validDateFromRange(dateUtils.yearsBeforeToday(13), upperBoundaryDate);
    }

    private LocalDate createItemChangeDate(String date) {
        LocalDate changeDate;
        switch(date.toUpperCase()) {
            case "VALID":
                changeDate = Phaker.validDateFromRange(dateUtils.yearsBeforeToday(13), TestConstants.DEFAULT_CUTOFF_DATE);
                break;
            case "FUTUREDATE":
                changeDate = dateUtils.daysAfterToday(1);
                break;
            default:
                LocalDate itemChangeDate = dateUtils.parse(date);
                if ( itemChangeDate == null ) {
                    throw new TestFailException("Invalid item change date: "+date);
                }
                changeDate = itemChangeDate;
        }
        return changeDate;
    }

    private String createDataItemChangeValue(String personDetailDataItemChanged, String origValue, String dataItemChangeValue) {
        String dataItemChangeFieldValue = "";
        if ( PersonDetailDataItemChangedEnum.fromString(personDetailDataItemChanged) == null ) {
            dataItemChangeFieldValue = personDetailDataItemChanged;
        } else {
            switch ( PersonDetailDataItemChangedEnum.fromString(personDetailDataItemChanged) ) {
                case GENDER:
                    dataItemChangeFieldValue = createGenderChangeOption(origValue, dataItemChangeValue);
                    break;
                case NAME:
                    dataItemChangeFieldValue = createNameChangeOption(origValue, dataItemChangeValue);
                    break;
                case DATE_OF_BIRTH:
                    dataItemChangeFieldValue = createDateOfBirthOption(origValue, dataItemChangeValue);
                    break;
                case DATE_OF_DEATH:
                    dataItemChangeFieldValue = createDateOfDeathOption(origValue, dataItemChangeValue);
                    break;
                default:
                    throw new TestFailException("Unsupported Person Detail Data Item Change Enum");
            }
        }
        return dataItemChangeFieldValue;
    }

    private String createDateOfDeathOption(String origValue, String option) {
        String dobFieldOption = "";
        switch(option.toUpperCase()) {
            case "VALID":
                dobFieldOption = Phaker.validDateFromRange(dateUtils.yearsBeforeToday(13), TestConstants.DEFAULT_CUTOFF_DATE).format(Phaker.DATETIME_FORMATTER_YYYYMMDD);
                break;
            case "SAME":
                dobFieldOption = origValue;
                break;
            default:
                if ( option.matches("^[0-9]{8}$") ) {
                    dobFieldOption = option;
                } else {
                    throw new TestFailException("Unsupported death date option: "+option);
                }
        }
        return dobFieldOption;
    }

    private String createDateOfBirthOption(String origValue, String option) {
        String dobFieldOption = "";
        switch(option.toUpperCase()) {
            case "VALID":
                dobFieldOption = Phaker.validDateFromRange(dateUtils.yearsBeforeToday(13), TestConstants.DEFAULT_CUTOFF_DATE).format(Phaker.DATETIME_FORMATTER_YYYYMMDD);
                break;
            case "SAME":
                dobFieldOption = origValue;
                break;
            default:
                if ( option.matches("^[0-9]{8}$") ) {
                    dobFieldOption = option;
                } else {
                    throw new TestFailException("Unsupported birth date option: "+option);
                }
        }
        return dobFieldOption;
    }

    private String createNameChangeOption(String origValue, String option) {
        String nameFieldOption = "";
        switch(option.toUpperCase()) {
            case "VALID":
                nameFieldOption = Phaker.validName();
                break;
            case "INVALID":
                nameFieldOption = "@#$%^&"+Phaker.validName();
            case "SAME":
                nameFieldOption = origValue;
            default:
                nameFieldOption = option;
        }
        return nameFieldOption;
    }

    private String createGenderChangeOption(String origValue, String option) {
        if ( Gender.fromString(origValue) == null ) {
            throw new TestFailException("Unsupported gender option: "+origValue);
        }
        String fieldOption = "";
        switch(option.toUpperCase()) {
            case "VALID":
                fieldOption = Arrays.stream(Gender.values())
                        .filter(gender -> !gender.equals(Gender.fromString(origValue)))
                        .findFirst()
                        .orElseThrow(()->new TestFailException("Unable to find a valid Gender enum that is not the same as the original value: "+origValue))
                        .getValue();
                break;
            case "SAME":
                fieldOption = origValue;
                break;
            case "INVALID":
                fieldOption = Gender.invalidGender();
                break;
            default:
                if ( Gender.fromString(option) == null ) {
                    throw new TestFailException("Unsupported person details option for gender change: "+option);
                } else {
                    fieldOption = Gender.fromString(option).getValue();
                }
        }
        return fieldOption;
    }

    private String nricFieldOptions(String nricOption) {
        String nricFieldOption = "";
        switch(nricOption.toUpperCase()) {
            case "VALID":
                nricFieldOption = Phaker.validNric();
                break;
            case "INVALID":
                nricFieldOption = Phaker.invalidNric();
                break;
            default:
                if ( nricOption.matches("^[S|T|F|G][0-9]{7}[A-Z]$") ) {
                    nricFieldOption = nricOption;
                } else {
                    throw new TestFailException("Unsupported nric option when building a file entry in the Mha Change Person Details file with following option: "+nricOption);
                }
        }
        return nricFieldOption;
    }
}
