package cdit_automation.data_helpers;

import cdit_automation.data_helpers.batch_entities.MhaNoInteractionFileEntry;
import cdit_automation.enums.PersonIdTypeEnum;
import cdit_automation.models.Batch;
import cdit_automation.models.Person;
import cdit_automation.models.PersonId;
import cdit_automation.models.embeddables.BiTemporalData;
import cdit_automation.utilities.CommonUtils;
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
                        CommonUtils.nricFieldOptions(map.get(FIELD_NRIC)),
                        CommonUtils.emptyStringIfInputIsKeywordBlank(map.get(FIELD_VALID_FROM_DATE)),
                        CommonUtils.emptyStringIfInputIsKeywordBlank(map.get(FIELD_VALID_TILL_DATE)));
        
        return mhaNoInteractionFileEntry.toString();
    }

    public Person getPerson() {
        return Person.create();
    }
    
    public PersonId getPersonId(Map<String, String> map, Batch batch, Person person) {

        return PersonId.create(
                batch,
                PersonIdTypeEnum.valueOf(map.get(FIELD_ID_TYPE)),
                person,
                map.get(FIELD_NRIC),
                new BiTemporalData()
                        .generateNewBiTemporalData(
                                dateUtils.beginningOfDayToTimestamp(dateUtils.yearsBeforeToday(10))));
    }
}
