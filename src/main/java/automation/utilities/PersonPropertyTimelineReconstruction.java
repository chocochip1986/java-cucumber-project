package automation.utilities;

import automation.configuration.datasource.AbstractAutoWired;
import automation.enums.datasource.PersonPropertyTypeEnum;
import automation.exceptions.TestFailException;
import automation.models.datasource.Person;
import automation.models.datasource.PersonProperty;
import automation.models.datasource.embeddables.BiTemporalData;
import automation.models.datasource.embeddables.BusinessTemporalData;
import automation.models.datasource.embeddables.DbTemporalData;
import automation.models.datasource.embeddables.PersonPropertyId;
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
                .businessTemporalData(new BusinessTemporalData(originalTimeLine.get(originalTimeLine.size()-1).getIdentifier().getValidFrom(), originalTimeLine.get(0).getValidTill()))
                .dbTemporalData(new DbTemporalData())
                .build();
        PersonPropertyId personPropertyIdTimeline = PersonPropertyId.builder()
                .type(PersonPropertyTypeEnum.RESIDENCE)
                .validFrom(originalTimeLine.get(originalTimeLine.size()-1).getIdentifier().getValidFrom())
                .build();
        PersonProperty personPropertyTimeline = PersonProperty.builder()
                .identifier(personPropertyIdTimeline)
                .validTill(originalTimeLine.get(0).getValidTill())
                .build();

        if ( (newPersonPropertyRecord.getIdentifier().getValidFrom().before(personPropertyTimeline.getIdentifier().getValidFrom()) &&
        newPersonPropertyRecord.getValidTill().before(personPropertyTimeline.getIdentifier().getValidFrom())) ||
            (newPersonPropertyRecord.getIdentifier().getValidFrom().after(personPropertyTimeline.getValidTill()) &&
            newPersonPropertyRecord.getValidTill().after(personPropertyTimeline.getValidTill()))) {
            return;
        }

        //TODO New record valid_from, valid_till falls entirely within a single record in the original timeline
        PersonProperty overlappedRecord = personPropertyRepo.findOverlappingRecord(newPersonPropertyRecord);
        if ( overlappedRecord != null ) {
            List<PersonProperty> slicedPersonProperties = new ArrayList<>();
            if ( overlappedRecord.getIdentifier().getValidFrom().before(newPersonPropertyRecord.getIdentifier().getValidFrom()) ) {
                PersonPropertyId personPropertyId = PersonPropertyId.builder()
                        .personEntity(person)
                        .propertyEntity(overlappedRecord.getIdentifier().getPropertyEntity())
                        .validFrom(overlappedRecord.getIdentifier().getValidFrom())
                        .type(overlappedRecord.getIdentifier().getType())
                        .build();
                slicedPersonProperties.add(PersonProperty.builder().
                        identifier(personPropertyId)
                        .batch(overlappedRecord.getBatch())
                        .validTill(Timestamp.valueOf(newPersonPropertyRecord.getIdentifier().getValidFrom().toLocalDateTime().minusDays(1)))
                        .build());
            }
            if ( overlappedRecord.getValidTill().after(newPersonPropertyRecord.getValidTill()) ) {
                PersonPropertyId personPropertyId = PersonPropertyId.builder()
                        .personEntity(person)
                        .propertyEntity(overlappedRecord.getIdentifier().getPropertyEntity())
                        .validFrom(Timestamp.valueOf(newPersonPropertyRecord.getValidTill().toLocalDateTime().plusDays(1)))
                        .type(overlappedRecord.getIdentifier().getType())
                        .build();
                slicedPersonProperties.add(PersonProperty.builder().
                        identifier(personPropertyId)
                        .batch(overlappedRecord.getBatch())
                        .validTill(overlappedRecord.getValidTill())
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
