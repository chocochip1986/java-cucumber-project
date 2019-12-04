package cdit_automation.data_helpers.factories;

import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.NationalityEnum;
import cdit_automation.enums.PersonIdTypeEnum;
import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.Batch;
import cdit_automation.models.Nationality;
import cdit_automation.models.Person;
import cdit_automation.models.PersonDetail;
import cdit_automation.models.PersonId;
import cdit_automation.models.PersonName;
import cdit_automation.models.embeddables.BiTemporalData;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

@Component
public class PersonFactory extends AbstractFactory {

    public PersonId createNewPersonId(String residentialStatus) {
        switch ( residentialStatus ) {
            case "SC":
                return createNewSCPersonId();
            case "FR":
                return createNewFRPersonId();
            case "PP":
                return createNewPPPersonId();
            default:
                return createNewSCPersonId();
        }
    }

    public PersonId createNewPPPersonId() {
        Batch batch = Batch.builder().build();
        Person person = Person.create();
        BiTemporalData biTemporalData = new BiTemporalData()
                .generateNewBiTemporalData(dateUtils.beginningOfDayToTimestamp(dateUtils.yearsBeforeToday(1)));
        PersonId personId = PersonId.create(PersonIdTypeEnum.PP, person, biTemporalData);
        PersonDetail personDetail = PersonDetail.create(batch, person, biTemporalData);
        PersonName personName = PersonName.create(batch, person, biTemporalData);
        Nationality nationality = Nationality.create(batch, person, NationalityEnum.PERMANENT_RESIDENT, biTemporalData, dateUtils.beginningOfDayToTimestamp(personDetail.getDateOfBirth()));

        batchRepo.save(batch);
        personRepo.save(person);
        personIdrepo.save(personId);
        personDetailRepo.save(personDetail);
        personNameRepo.save(personName);
        nationalityRepo.save(nationality);

        return personId;
    }

    public PersonId createNewFRPersonId() {
        Batch batch = Batch.builder().build();
        Person person = Person.create();
        BiTemporalData biTemporalData = new BiTemporalData()
                .generateNewBiTemporalData(dateUtils.beginningOfDayToTimestamp(dateUtils.yearsBeforeToday(1)));
        PersonId personId = PersonId.create(PersonIdTypeEnum.FIN, person, biTemporalData);
        PersonDetail personDetail = PersonDetail.create(batch, person, biTemporalData);
        PersonName personName = PersonName.create(batch, person, biTemporalData);
        Nationality nationality = Nationality.create(batch, person, NationalityEnum.NON_SINGAPORE_CITIZEN, biTemporalData, dateUtils.beginningOfDayToTimestamp(personDetail.getDateOfBirth()));

        batchRepo.save(batch);
        personRepo.save(person);
        personDetailRepo.save(personDetail);
        nationalityRepo.save(nationality);
        personIdrepo.save(personId);
        personNameRepo.save(personName);

        return personId;
    }

    public PersonId createNewSCPersonId() {
        Batch batch = Batch.builder().build();
        Person person = Person.create();
        BiTemporalData biTemporalData = new BiTemporalData()
                .generateNewBiTemporalData(dateUtils.beginningOfDayToTimestamp(dateUtils.yearsBeforeToday(1)));
        PersonId personId = PersonId.create(PersonIdTypeEnum.NRIC, person, biTemporalData);
        PersonDetail personDetail = PersonDetail.create(batch, person, biTemporalData);
        PersonName personName = PersonName.create(batch, person, biTemporalData);
        Nationality nationality = Nationality.create(batch, person, NationalityEnum.SINGAPORE_CITIZEN, biTemporalData, dateUtils.beginningOfDayToTimestamp(personDetail.getDateOfBirth()));

        batchRepo.save(batch);
        personRepo.save(person);
        personDetailRepo.save(personDetail);
        nationalityRepo.save(nationality);
        personIdrepo.save(personId);
        personNameRepo.save(personName);

        return personId;
    }

    public PersonId createDualCitizen() {
        Batch batch = Batch.builder().build();
        Person person = Person.create();
        BiTemporalData biTemporalData = new BiTemporalData()
                .generateNewBiTemporalData(dateUtils.beginningOfDayToTimestamp(dateUtils.yearsBeforeToday(1)));
        PersonId personId = PersonId.create(PersonIdTypeEnum.NRIC, person, biTemporalData);
        PersonDetail personDetail = PersonDetail.create(batch, person, biTemporalData);
        PersonName personName = PersonName.create(batch, person, biTemporalData);
        Nationality nationality = Nationality.create(batch, person, NationalityEnum.DUAL_CITIZENSHIP, biTemporalData, dateUtils.beginningOfDayToTimestamp(personDetail.getDateOfBirth()));

        batchRepo.save(batch);
        personRepo.save(person);
        personDetailRepo.save(personDetail);
        nationalityRepo.save(nationality);
        personIdrepo.save(personId);
        personNameRepo.save(personName);

        return personId;
    }
}
