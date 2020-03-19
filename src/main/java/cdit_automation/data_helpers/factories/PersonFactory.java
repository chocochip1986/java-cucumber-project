package cdit_automation.data_helpers.factories;

import cdit_automation.constants.TestConstants;
import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.GenderEnum;
import cdit_automation.enums.NationalityEnum;
import cdit_automation.enums.PersonIdTypeEnum;
import cdit_automation.models.Batch;
import cdit_automation.models.Gender;
import cdit_automation.models.Nationality;
import cdit_automation.models.Person;
import cdit_automation.models.PersonDetail;
import cdit_automation.models.PersonId;
import cdit_automation.models.PersonName;
import cdit_automation.models.embeddables.BiTemporalData;
import cdit_automation.models.embeddables.BusinessTemporalData;
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
        private GenderEnum gender;
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

        public void setNationalityAndIdentifier(NationalityEnum nationality, String identifier) {
            if ( nationality == null && identifier != null ) {
                this.identifier = identifier;
                decideNationalityBasedOnIdentifier();
            }
            else if ( nationality != null && identifier == null ) {
                this.nationality = nationality;
                decideIdentifierBasedOnNationality();
            }
            else if ( nationality != null && identifier != null ) {
                this.nationality = nationality;
                this.identifier = identifier;
            }
        }

        private void decideNationalityBasedOnIdentifier() {
            if ( this.identifier.matches("^[F|G][0-9]{7}[A-Z]$") && !this.nationality.equals(NationalityEnum.NON_SINGAPORE_CITIZEN) ) {
                this.nationality = NationalityEnum.NON_SINGAPORE_CITIZEN;
            }
            if ( !(this.nationality.equals(NationalityEnum.SINGAPORE_CITIZEN) ||
                    this.nationality.equals(NationalityEnum.PERMANENT_RESIDENT) ||
                    this.nationality.equals(NationalityEnum.DUAL_CITIZENSHIP)) &&
                    this.identifier.matches("^[S|T][0-9]{7}[A-Z]$") ) {
                this.nationality = NationalityEnum.SINGAPORE_CITIZEN;
            }
        }

        private void decideIdentifierBasedOnNationality() {
            if ( this.nationality.equals(NationalityEnum.NON_SINGAPORE_CITIZEN) ) {
                this.identifier = Phaker.validFin();
            }
            if ( this.nationality.equals(NationalityEnum.SINGAPORE_CITIZEN) ||
                    this.nationality.equals(NationalityEnum.PERMANENT_RESIDENT) ||
                    this.nationality.equals(NationalityEnum.DUAL_CITIZENSHIP) ) {
                this.identifier = Phaker.validNric();
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

    public PersonId createDualCitzenTurnSC(LocalDate ceasedDualCitizenDate) {
        PersonId personId = createDualCitizen();
        Nationality nationality = nationalityRepo.findNationalityByPerson(personId.getPerson());
        BusinessTemporalData businessTemporalData = nationality.getBiTemporalData().getBusinessTemporalData();
        nationality.getBiTemporalData().setBusinessTemporalData(businessTemporalData.newValidTill(dateUtils.endOfDayToTimestamp(ceasedDualCitizenDate.minusDays(1))));

        Batch batch = Batch.createCompleted();
        BiTemporalData biTemporalData = new BiTemporalData().generateNewBiTemporalData(dateUtils.beginningOfDayToTimestamp(ceasedDualCitizenDate));
        Nationality newNationality = Nationality.builder()
                .nationality(NationalityEnum.SINGAPORE_CITIZEN)
                .biTemporalData(biTemporalData)
                .person(personId.getPerson())
                .batch(batch)
                .build();
        batchRepo.save(batch);
        nationalityRepo.save(nationality);
        nationalityRepo.save(newNationality);
        return personId;
    }

    public PersonId createNewPPPersonId() {
        return createNewPPPersonId(null, null, null, null, null);
    }

    public PersonId createNewPPPersonId(String nric) {
        return createNewPPPersonId(null, null, null, null, nric);
    }

    public PersonId createNewPPPersonId(LocalDate birthDate, LocalDate deathDate, String name, GenderEnum gender, String identifier) {
        return createPersonData(birthDate, deathDate, name, gender, NationalityEnum.PERMANENT_RESIDENT, identifier);
    }

    public PersonId createNewFRPersonId() {
        return createNewFRPersonId(null, null, null, null, null);
    }

    public PersonId createNewFRPersonId(String fin) {
        return createNewFRPersonId(null, null, null, null, fin);
    }

    public PersonId createNewFRPersonId(LocalDate birthDate, LocalDate deathDate, String name, GenderEnum gender, String identifier) {
        return createPersonData(birthDate, deathDate, name, gender, NationalityEnum.NON_SINGAPORE_CITIZEN, identifier);
    }

    public PersonId createNewFRPersonId(LocalDate birthDate) {
        return createPersonData(birthDate, null, null,  null, NationalityEnum.NON_SINGAPORE_CITIZEN, null);
    }

    public PersonId createNewFRPersonId(LocalDate birthDate, String name) {
        return createPersonData(birthDate, null, name,  null, NationalityEnum.NON_SINGAPORE_CITIZEN, null);
    }

    public PersonId createNewSCPersonId() {
        return createNewSCPersonId(null, null, null, null, null);
    }

    public PersonId createNewSCPersonId(String nric) {
        return createNewSCPersonId(null, null, null, null, nric);
    }

    public PersonId createNewSCPersonId(LocalDate birthDate) {
        return createPersonData(birthDate, null, null, null, NationalityEnum.SINGAPORE_CITIZEN, null);
    }

    public PersonId createNewSCPersonId(LocalDate birthDate, String name) {
        return createPersonData(birthDate, null, name, null, NationalityEnum.SINGAPORE_CITIZEN, null);
    }

    public PersonId createNewSCPersonId(LocalDate birthDate, LocalDate deathDate, String name, GenderEnum gender, String identifier) {
        return createPersonData(birthDate, deathDate, name, gender, NationalityEnum.SINGAPORE_CITIZEN, identifier);
    }

    public PersonId createDualCitizen() {
        return createDualCitizen(null, null, null, null, null);
    }

    public PersonId createDualCitizen(String identifier) {
        return createDualCitizen(null, null, null, null, identifier);
    }

    public PersonId createDualCitizen(LocalDate birthDate, LocalDate deathDate, String name, GenderEnum gender, String identifier) {
        return createPersonData(birthDate, deathDate, name, gender, NationalityEnum.DUAL_CITIZENSHIP, identifier);
    }

    public void updateBirthdate(Person person, LocalDate birthDate, LocalDate oldValidFrom) {
        personDetailRepo.updateBirthDateForPerson(birthDate, person);
        personIdRepo.updateValidFrom(dateUtils.localDateToDate(birthDate), person, dateUtils.localDateToDate(oldValidFrom));
        personNameRepo.updateValidFrom(dateUtils.localDateToDate(birthDate), person, dateUtils.localDateToDate(oldValidFrom));
        nationalityRepo.updateValidFrom(dateUtils.localDateToDate(birthDate), person, dateUtils.localDateToDate(oldValidFrom));
    }

    public PersonId createPersonData(LocalDate birthDate, LocalDate deathDate, String name, GenderEnum gender, NationalityEnum nationalityEnum, String identifier) {
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
        if ( name != null ) {
            personOptions.setName(name);
        }
        personOptions.setNationalityAndIdentifier(nationalityEnum, identifier);

        return createPerson(personOptions);
    }

    private PersonId createPerson(PersonOptions personOptions) {
        Batch batch = Batch.createCompleted();
        Person person = Person.create();
        BiTemporalData biTemporalData = new BiTemporalData()
                .generateNewBiTemporalData(dateUtils.beginningOfDayToTimestamp(personOptions.getBirthDate()));
        PersonId personId = PersonId.create(mapNationalityToPersonIdType(personOptions.getNationality()), person, personOptions.getIdentifier(), biTemporalData);
        PersonDetail personDetail = PersonDetail.create(batch, person, personOptions.getBirthDate(), personOptions.getDeathDate(), biTemporalData);
        Gender gender = Gender.create(personOptions.gender, person, batch, biTemporalData);
        PersonName personName = PersonName.create(batch, person, personOptions.getName(), biTemporalData);
        Nationality nationality = Nationality.create(batch, person, personOptions.getNationality(), biTemporalData, dateUtils.beginningOfDayToTimestamp(personOptions.getBirthDate()));

        batchRepo.save(batch);
        personRepo.save(person);
        genderRepo.save(gender);
        personDetailRepo.save(personDetail);
        nationalityRepo.save(nationality);
        personIdRepo.save(personId);
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
