package cdit_automation.data_helpers.factories;

import cdit_automation.constants.TestConstants;
import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.Gender;
import cdit_automation.enums.NationalityEnum;
import cdit_automation.enums.PersonIdTypeEnum;
import cdit_automation.models.Batch;
import cdit_automation.models.Nationality;
import cdit_automation.models.Person;
import cdit_automation.models.PersonDetail;
import cdit_automation.models.PersonId;
import cdit_automation.models.PersonName;
import cdit_automation.models.embeddables.BiTemporalData;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PersonFactory extends AbstractFactory {
    @Getter
    @Setter
    private class PersonOptions {
        private LocalDate birthDate;
        private LocalDate deathDate;
        private String name;
        private Gender gender;
        private NationalityEnum nationality;
        private String identifier;

        private PersonOptions() {
            this.birthDate = Phaker.validDateFromRange(dateUtils.yearsBeforeToday(13), TestConstants.DEFAULT_CUTOFF_DATE);
            this.deathDate = null;
            this.name = Phaker.validName();
            this.gender = Phaker.validGender();
            this.nationality = NationalityEnum.SINGAPORE_CITIZEN;
            this.identifier = null;
        }

        public String getIdentifier(){
            //The getNric() function also stores the nric generated because Phaker will keep a list of unique NRICs generated for the lifetime of the automation run. I don't want to waste it.
            if ( this.identifier == null ) {
                this.identifier = Phaker.validNric();
                return this.identifier;
            } else {
                return this.identifier;
            }
        }
    }

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
        return createNewPPPersonId(null, null, null, null, null);
    }

    public PersonId createNewPPPersonId(String nric) {
        return createNewPPPersonId(null, null, null, null, nric);
    }

    public PersonId createNewPPPersonId(LocalDate birthDate, LocalDate deathDate, String name, Gender gender, String identifier) {
        return createPersonData(birthDate, deathDate, name, gender, NationalityEnum.PERMANENT_RESIDENT, identifier);
    }

    public PersonId createNewFRPersonId() {
        return createNewFRPersonId(null, null, null, null, null);
    }

    public PersonId createNewFRPersonId(String fin) {
        return createNewFRPersonId(null, null, null, null, fin);
    }

    public PersonId createNewFRPersonId(LocalDate birthDate, LocalDate deathDate, String name, Gender gender, String identifier) {
        return createPersonData(birthDate, deathDate, name, gender, NationalityEnum.NON_SINGAPORE_CITIZEN, identifier);
    }

    public PersonId createNewSCPersonId() {
        return createNewSCPersonId(null, null, null, null, null);
    }

    public PersonId createNewSCPersonId(String nric) {
        return createNewSCPersonId(null, null, null, null, nric);
    }

    public PersonId createNewSCPersonId(LocalDate birthDate, LocalDate deathDate, String name, Gender gender, String identifier) {
        return createPersonData(birthDate, deathDate, name, gender, NationalityEnum.SINGAPORE_CITIZEN, identifier);
    }

    public PersonId createDualCitizen() {
        return createDualCitizen(null, null, null, null, null);
    }

    public PersonId createDualCitizen(String identifier) {
        return createDualCitizen(null, null, null, null, identifier);
    }

    public PersonId createDualCitizen(LocalDate birthDate, LocalDate deathDate, String name, Gender gender, String identifier) {
        return createPersonData(birthDate, deathDate, name, gender, NationalityEnum.DUAL_CITIZENSHIP, identifier);
    }

    public PersonId createPersonData(LocalDate birthDate, LocalDate deathDate, String name, Gender gender, NationalityEnum nationalityEnum, String identifier) {
        PersonOptions personOptions = new PersonOptions();
        if ( birthDate != null ) {
            personOptions.setBirthDate(birthDate);
        }
        if ( deathDate != null ) {
            personOptions.setDeathDate(deathDate);
        }
        if ( gender != null ) {
            personOptions.setGender(gender);
        }
        if ( identifier != null ) {
            personOptions.setIdentifier(identifier);
        }
        if ( nationalityEnum != null ) {
            personOptions.setNationality(nationalityEnum);
        }
        return createPerson(personOptions);
    }

    private PersonId createPerson(PersonOptions personOptions) {
        Batch batch = Batch.createCompleted();
        Person person = Person.create();
        BiTemporalData biTemporalData = new BiTemporalData()
                .generateNewBiTemporalData(dateUtils.beginningOfDayToTimestamp(personOptions.getBirthDate()));
        PersonId personId = PersonId.create(mapNationalityToPersonIdType(personOptions.getNationality()), person, personOptions.getIdentifier(), biTemporalData);
        PersonDetail personDetail = PersonDetail.create(batch, person, personOptions.getBirthDate(), personOptions.getDeathDate(), personOptions.getGender(), false, biTemporalData);
        PersonName personName = PersonName.create(batch, person, personOptions.getName(), biTemporalData);
        Nationality nationality = Nationality.create(batch, person, personOptions.getNationality(), biTemporalData, dateUtils.beginningOfDayToTimestamp(personOptions.getBirthDate()));

        batchRepo.save(batch);
        personRepo.save(person);
        personDetailRepo.save(personDetail);
        nationalityRepo.save(nationality);
        personIdrepo.save(personId);
        personNameRepo.save(personName);

        return personId;
    }

    private PersonIdTypeEnum mapNationalityToPersonIdType(NationalityEnum nationalityEnum) {
        if ( nationalityEnum == NationalityEnum.NON_SINGAPORE_CITIZEN ) {
            return PersonIdTypeEnum.FIN;
        } else if ( nationalityEnum == NationalityEnum.SINGAPORE_CITIZEN || nationalityEnum == NationalityEnum.DUAL_CITIZENSHIP ) {
            return PersonIdTypeEnum.NRIC;
        } else if ( nationalityEnum == NationalityEnum.PERMANENT_RESIDENT ) {
            return PersonIdTypeEnum.PP;
        } else {
            return PersonIdTypeEnum.FIN;
        }
    }
}
