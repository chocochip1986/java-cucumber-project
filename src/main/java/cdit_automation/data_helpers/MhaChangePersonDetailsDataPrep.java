package cdit_automation.data_helpers;

import cdit_automation.constants.TestConstants;
import cdit_automation.data_helpers.batch_entities.MhaChangePersonDetailsFileEntry;
import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.Gender;
import cdit_automation.enums.PersonDetailDataItemChangedEnum;
import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.PersonId;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class MhaChangePersonDetailsDataPrep extends BatchFileDataPrep {
    public List<String> createBodyOfTestScenarios(List<Map<String, String>> listOfScenarios) {
        List<String> body = new ArrayList<>();

        for (Map<String, String> scenario : listOfScenarios) {
            body.add(createScenarioEntry(scenario));
        }
        return body;
    }

    public String createScenarioEntry(Map<String, String> scenario) {
        MhaChangePersonDetailsFileEntry mhaChangePersonDetailsFileEntry =
                new MhaChangePersonDetailsFileEntry(
                        nricFieldOptions(scenario.get("nric")),
                        scenario.get("data_item_orig_value"),
                        createDataItemChangeValue(PersonDetailDataItemChangedEnum.fromString(scenario.get("data_item_change_category")),
                                scenario.get("data_item_orig_value"), scenario.get("data_item_change_value")),
                        createItemChangeDate(scenario.get("data_item_change_date")),
                        createDataItemChangeCategory(scenario.get("data_item_change_category")));
        //Generate the fake data
        setupDateForScenario(mhaChangePersonDetailsFileEntry.getDataItemCat(), mhaChangePersonDetailsFileEntry);
        return mhaChangePersonDetailsFileEntry.toString();
    }

    private void setupDateForScenario(PersonDetailDataItemChangedEnum itemChangeCat, MhaChangePersonDetailsFileEntry entry) {
        PersonId personId = personFactory.createNewSCPersonId(entry.getNric());
        switch (itemChangeCat) {
            case GENDER:
                Gender originalGender = Gender.fromString(entry.getDataItemOriginalValue());
                personDetailRepo.updateGenderForPerson(originalGender, personId.getPerson());
                break;
            case NAME:
                personNameRepo.updateNameForPerson(entry.getDataItemOriginalValue(), personId.getPerson());
                break;
            case DATE_OF_BIRTH:
                LocalDate origBirthDate = dateUtils.parse(entry.getDataItemOriginalValue());
                personFactory.updateBirthdate(personId.getPerson(), origBirthDate, origBirthDate);
                break;
            case DATE_OF_DEATH:
                personDetailRepo.updateDeathDateForPerson(dateUtils.parse(entry.getDataItemOriginalValue()), personId.getPerson());
                break;
            default:
                throw new TestFailException("No such Person Details Data Item Change category: "+itemChangeCat);
        }
    }

    private String createItemChangeDate(String date) {
        String changeDate = "";
        switch(date.toUpperCase()) {
            case "VALID":
                changeDate = Phaker.validDateFromRange(dateUtils.yearsBeforeToday(13), TestConstants.DEFAULT_CUTOFF_DATE).format(dateUtils.DATETIME_FORMATTER_YYYYMMDD);
                break;
            case "FUTUREDATE":
                changeDate = dateUtils.daysAfterToday(1).format(dateUtils.DATETIME_FORMATTER_YYYYMMDD);
                break;
            default:
                LocalDate itemChangeDate = dateUtils.parse(date);
                if ( itemChangeDate == null ) {
                    throw new TestFailException("Invalid item change date: "+date);
                }
                changeDate = itemChangeDate.format(dateUtils.DATETIME_FORMATTER_YYYYMMDD);
        }
        return changeDate;
    }

    private String createDataItemChangeValue(PersonDetailDataItemChangedEnum personDetailDataItemChangedEnum, String origValue, String dataItemChangeValue) {
        String dataItemChangeFieldValue = "";
        switch ( personDetailDataItemChangedEnum ) {
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
                throw new TestFailException("Invalid person details change data item option: "+personDetailDataItemChangedEnum.getValue());
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

    private PersonDetailDataItemChangedEnum createDataItemChangeCategory(String dataItemChangeCategoryOption) {
        PersonDetailDataItemChangedEnum dataItemChangeCategoryFieldOption = PersonDetailDataItemChangedEnum.fromString(dataItemChangeCategoryOption);
        if ( dataItemChangeCategoryFieldOption == null ) {
            throw new TestFailException("Unsupported data item change category option: "+dataItemChangeCategoryOption);
        }
        return dataItemChangeCategoryFieldOption;
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
                if ( nricOption.matches("^[S|T|F|G][0-9][A-Z]$") ) {
                    nricFieldOption = nricOption;
                } else {
                    throw new TestFailException("Unsupported option when building a file entry in the Mha Change Person Details file with following option: "+nricFieldOption);
                }
        }
        return nricFieldOption;
    }
}
