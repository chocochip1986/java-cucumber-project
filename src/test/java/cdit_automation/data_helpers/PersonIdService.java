package cdit_automation.data_helpers;

import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.NationalityEnum;
import cdit_automation.enums.PersonIdTypeEnum;
import cdit_automation.enums.RestrictedEnum;
import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.Batch;
import cdit_automation.models.Nationality;
import cdit_automation.models.Person;
import cdit_automation.models.PersonDetail;
import cdit_automation.models.PersonId;
import cdit_automation.models.PersonName;
import cdit_automation.models.embeddables.BiTemporalData;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

@Service
public class PersonIdService extends AbstractService {

    private final String NRIC_PATTERN = "^[S|T][0-9]{7}[A-Z]$";
    private final String FIN_PATTERN = "^[F|G][0-9]{7}[A-Z]$";

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
        PersonId personId = createPersonId(person, PersonIdTypeEnum.PP);
        PersonDetail personDetail = createPersonDetail(batch, person);
        PersonName personName = createPersonName(batch, person);
        Nationality nationality = createNationality(batch, person, NationalityEnum.PERMANENT_RESIDENT);

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
        PersonId personId = createPersonId(person, PersonIdTypeEnum.FIN);
        PersonDetail personDetail = createPersonDetail(batch, person);
        PersonName personName = createPersonName(batch, person);
        Nationality nationality = createNationality(batch, person, NationalityEnum.NON_SINGAPORE_CITIZEN);

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
        PersonId personId = createPersonId(person, PersonIdTypeEnum.NRIC );
        PersonDetail personDetail = createPersonDetail(batch, person);
        PersonName personName = createPersonName(batch, person);
        Nationality nationality = createNationality(batch, person, NationalityEnum.SINGAPORE_CITIZEN);

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
        Nationality nationality = createNationality(batch, person, NationalityEnum.DUAL_CITIZENSHIP);
        PersonDetail personDetail = createPersonDetail(batch, person);
        PersonName personName = createPersonName(batch, person);
        PersonId personId = createPersonId(person, PersonIdTypeEnum.NRIC);

        batchRepo.save(batch);
        personRepo.save(person);
        personDetailRepo.save(personDetail);
        nationalityRepo.save(nationality);
        personIdrepo.save(personId);
        personNameRepo.save(personName);

        return personId;
    }

    private Nationality createNationality(@NotNull Batch batch, @NotNull Person person, @NotNull NationalityEnum nationality) {
        return Nationality.builder()
                .nationality(nationality)
                .batch(batch)
                .person(person)
                .biTemporalData(new BiTemporalData()
                        .generateNewBiTemporalData(dateUtils.beginningOfDayToTimestamp(dateUtils.yearsBeforeToday(1))))
                .citizenshipAttainmentDate(dateUtils.beginningOfDayToTimestamp(dateUtils.yearsBeforeToday(10)))
                .build();
    }

    private PersonId createPersonId(@NotNull Person person, @NotNull PersonIdTypeEnum identifierType) {

        PersonId personId = PersonId.builder()
                .person(person)
                .personIdType(PersonIdTypeEnum.NRIC)
                .biTemporalData(new BiTemporalData()
                        .generateNewBiTemporalData(dateUtils.beginningOfDayToTimestamp(dateUtils.yearsBeforeToday(1))))
                .build();

        switch (identifierType) {
            case NRIC:
                personId.setNaturalId(Phaker.validNric());
                break;
            case FIN:
                personId.setNaturalId(Phaker.validFin());
                break;
            case PP:
                personId.setNaturalId(Phaker.validNric());
            default:
                throw new TestFailException("No support for PersonIdType: "+identifierType);
        }

        return personId;
    }

    private PersonDetail createPersonDetail(@NotNull Batch batch, @NotNull Person person) {
        return PersonDetail.builder()
                .batch(batch)
                .gender(Phaker.validGender())
                .isNricCancelled(false)
                .dateOfBirth(Phaker.validPastDate())
                .dateOfDeath(null)
                .person(person)
                .biTemporalData(new BiTemporalData()
                        .generateNewBiTemporalData(dateUtils.beginningOfDayToTimestamp(dateUtils.yearsBeforeToday(1))))
                .build();
    }

    private PersonName createPersonName(Batch batch, Person person) {
        return createPersonName(batch, person, null);
    }

    private PersonName createPersonName(Batch batch, Person person, @Nullable String name) {
        return PersonName.builder()
                .batch(batch)
                .name(name == null ? Phaker.validName() : name)
                .person(person)
                .biTemporalData(new BiTemporalData().generateNewBiTemporalData(dateUtils.beginningOfDayToTimestamp(dateUtils.yearsBeforeToday(1))))
                .build();

    }
}
