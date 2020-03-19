package cdit_automation.utilities;

import cdit_automation.configuration.AbstractAutoWired;
import cdit_automation.enums.PersonPropertyTypeEnum;
import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.Person;
import cdit_automation.models.PersonProperty;
import cdit_automation.models.embeddables.BiTemporalData;
import cdit_automation.models.embeddables.BusinessTemporalData;
import cdit_automation.models.embeddables.DbTemporalData;
import cdit_automation.models.embeddables.PersonPropertyId;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/*
Only allow this kind of scenario
1) New record valid_from, valid_till falls entirely outside the original timeline
2) New record valid_from, valid_till falls entirely within a single record in the original timeline
 */

@Component
public class PersonPropertyTimelineReconstruction extends AbstractAutoWired {
    public void reconstructResidenceTimelineFor(Person person, List<PersonProperty> originalTimeLine, PersonProperty newPersonPropertyRecord) {
        doNothingIfTimelineIsEmpty(originalTimeLine);
        errorOutIfNewRecordEnvelopsEntireTimeline(newPersonPropertyRecord);

        //TODO New record valid_from, valid_till falls entirely outside the original timeline
        BiTemporalData biTemporalData = BiTemporalData.builder()
                .businessTemporalData(new BusinessTemporalData(originalTimeLine.get(originalTimeLine.size()-1).getBiTemporalData().getBusinessTemporalData().getValidFrom(), originalTimeLine.get(0).getBiTemporalData().getBusinessTemporalData().getValidTill()))
                .dbTemporalData(new DbTemporalData())
                .build();
        PersonProperty personPropertyTimeline = PersonProperty.builder()
                .type(PersonPropertyTypeEnum.RESIDENCE)
                .biTemporalData(biTemporalData)
                .build();

        if ( (newPersonPropertyRecord.getBiTemporalData().getBusinessTemporalData().getValidFrom().before(personPropertyTimeline.getBiTemporalData().getBusinessTemporalData().getValidFrom()) &&
        newPersonPropertyRecord.getBiTemporalData().getBusinessTemporalData().getValidTill().before(personPropertyTimeline.getBiTemporalData().getBusinessTemporalData().getValidFrom())) ||
            (newPersonPropertyRecord.getBiTemporalData().getBusinessTemporalData().getValidFrom().after(personPropertyTimeline.getBiTemporalData().getBusinessTemporalData().getValidTill()) &&
            newPersonPropertyRecord.getBiTemporalData().getBusinessTemporalData().getValidTill().after(personPropertyTimeline.getBiTemporalData().getBusinessTemporalData().getValidTill()))) {
            return;
        }

        //TODO New record valid_from, valid_till falls entirely within a single record in the original timeline
        PersonProperty overlappedRecord = personPropertyRepo.findOverlappingRecord(newPersonPropertyRecord);
        if ( overlappedRecord != null ) {
            List<PersonProperty> slicedPersonProperties = new ArrayList<>();
            if ( overlappedRecord.getBiTemporalData().getBusinessTemporalData().getValidFrom().before(newPersonPropertyRecord.getBiTemporalData().getBusinessTemporalData().getValidFrom()) ) {
                PersonPropertyId personPropertyId = PersonPropertyId.builder().personEntity(person).propertyEntity(overlappedRecord.getIdentifier().getPropertyEntity()).build();
                slicedPersonProperties.add(PersonProperty.builder().
                        identifier(personPropertyId)
                        .batch(overlappedRecord.getBatch())
                        .biTemporalData(overlappedRecord.getBiTemporalData()
                                .generateNewBiTemporalData(
                                        overlappedRecord.getBiTemporalData().getBusinessTemporalData().getValidFrom(),
                                        Timestamp.valueOf(newPersonPropertyRecord.getBiTemporalData().getBusinessTemporalData().getValidFrom().toLocalDateTime().minusDays(1))))
                        .type(overlappedRecord.getType())
                        .build());
            }
            if ( overlappedRecord.getBiTemporalData().getBusinessTemporalData().getValidTill().after(newPersonPropertyRecord.getBiTemporalData().getBusinessTemporalData().getValidTill()) ) {
                PersonPropertyId personPropertyId = PersonPropertyId.builder().personEntity(person).propertyEntity(overlappedRecord.getIdentifier().getPropertyEntity()).build();
                slicedPersonProperties.add(PersonProperty.builder().
                        identifier(personPropertyId)
                        .batch(overlappedRecord.getBatch())
                        .biTemporalData(overlappedRecord.getBiTemporalData()
                                .generateNewBiTemporalData(
                                        Timestamp.valueOf(newPersonPropertyRecord.getBiTemporalData().getBusinessTemporalData().getValidTill().toLocalDateTime().plusDays(1)),
                                        overlappedRecord.getBiTemporalData().getBusinessTemporalData().getValidTill()))
                        .type(overlappedRecord.getType())
                        .build());
            }
            personPropertyRepo.deleteByPersonAndPropertyAndBatch(overlappedRecord);
            personPropertyRepo.saveAll(slicedPersonProperties);
        }

    }

    private void doNothingIfTimelineIsEmpty(List<PersonProperty> timeline) {
        if ( timeline.isEmpty() ) {
            return;
        }
    }

    private void errorOutIfNewRecordEnvelopsEntireTimeline(PersonProperty newPersonPropertyRecord) {
        PersonProperty personProperty = personPropertyRepo.findErrorenousOverlap(newPersonPropertyRecord);
        if ( personProperty != null ) {
            throw new TestFailException("Data setup error! The new person property record envelops the entire original person property timeline!");
        }
    }

    private void doNothingIfListIsEmpty(List<PersonProperty> personPropertyList) {
        if (isListEmpty(personPropertyList)) {
            return;
        }
    }

    private boolean isListEmpty(List<PersonProperty> list) {
        return list.isEmpty();
    }
}
