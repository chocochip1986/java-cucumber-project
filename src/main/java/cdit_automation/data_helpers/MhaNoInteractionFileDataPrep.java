package cdit_automation.data_helpers;

import cdit_automation.data_helpers.batch_entities.MhaNoInteractionFileEntry;
import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.PersonIdTypeEnum;
import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.Person;
import cdit_automation.models.PersonId;
import cdit_automation.models.embeddables.BiTemporalData;
import cdit_automation.utilities.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class MhaNoInteractionFileDataPrep extends BatchFileDataPrep {

    private static final String FIELD_NRIC = "nric";
    private static final String FIELD_VALID_FROM_DATE = "valid_from_date";
    private static final String FIELD_VALID_TILL_DATE = "valid_till_date";
    private static final String FIELD_ID_TYPE = "id_type";

    private static final String VALID = "VALID";
    private static final String INVALID = "INVALID";
    private static final String BLANK = "BLANK";
    private static final String SPACES = "SPACES";
    
    public List<String> createBodyOfTestScenarios(List<Map<String, String>> scenarioList) {
        
        List<String> body = new ArrayList<>();
        for (Map<String, String> scenario : scenarioList) {
            body.add(createScenarioEntry(scenario));
        }
        return body;
    }

    public String createScenarioEntry(Map<String, String> map) {
        
        MhaNoInteractionFileEntry mhaNoInteractionFileEntry =
                new MhaNoInteractionFileEntry(
                        nricFieldOptions(map.get(FIELD_NRIC)),
                        emptyStringIfInputIsKeywordBlank(map.get(FIELD_VALID_FROM_DATE)),
                        emptyStringIfInputIsKeywordBlank(map.get(FIELD_VALID_TILL_DATE)));
        
        return mhaNoInteractionFileEntry.toString();
    }

    public Person getPerson() {
        return Person.create();
    }
    
    public PersonId getPersonId(Map<String, String> map, Person person) {

        return PersonId.create(
                PersonIdTypeEnum.valueOf(map.get(FIELD_ID_TYPE)),
                person,
                map.get(FIELD_NRIC),
                new BiTemporalData()
                        .generateNewBiTemporalData(
                                dateUtils.beginningOfDayToTimestamp(dateUtils.yearsBeforeToday(10))));
    }

    public String emptyStringIfInputIsKeywordBlank(String detailString) {
        
        if (BLANK.equalsIgnoreCase(detailString)) {
            return "";
        }
        
        return detailString;
    }
    
    private String nricFieldOptions(String nricOption) {
        
        String nricValue = "";
        
        switch(nricOption.toUpperCase()) {
            case VALID:
                nricValue = Phaker.validNric();
                break;
            case INVALID:
                nricValue = Phaker.invalidNric();
                break;
            case SPACES:
                nricValue = StringUtils.rightPad(StringUtils.SPACE, 9);
                break;
            case BLANK:
                nricValue = "";
                break;
            default:
                if (nricOption.matches("^[STFG]\\d{7}[A-Z]$")) {
                    nricValue = nricOption;
                } else {
                    throw new TestFailException("Unsupported option when building a file entry in the Mha Change Person Details file with following option: " + nricOption);
                }
        }
        return nricValue;
    }
}
