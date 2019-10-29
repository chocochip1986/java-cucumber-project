package cdit_automation.data_helpers;

import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.NationalityEnum;
import cdit_automation.enums.PersonIdTypeEnum;
import cdit_automation.enums.RestrictedEnum;
import cdit_automation.models.Batch;
import cdit_automation.models.Nationality;
import cdit_automation.models.Person;
import cdit_automation.models.PersonId;
import org.springframework.stereotype.Service;

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
        Person person = Person.builder()
                .restricted(RestrictedEnum.NORMAL)
                .build();
        personRepo.save(person);

        PersonId personId = PersonId.builder()
                .naturalId(Phaker.validNric())
                .personIdType(PersonIdTypeEnum.PP)
                .person(person)
                .build();
        personIdrepo.save(personId);

        return personId;
    }

    public PersonId createNewFRPersonId() {
        Person person = Person.builder()
                .restricted(RestrictedEnum.NORMAL)
                .build();
        personRepo.save(person);

        PersonId personId = PersonId.builder()
                .naturalId(Phaker.validFin())
                .personIdType(PersonIdTypeEnum.FIN)
                .person(person)
                .build();
        personIdrepo.save(personId);

        return personId;
    }

    public PersonId createNewSCPersonId() {
        Person person = Person.builder()
                .restricted(RestrictedEnum.NORMAL)
                .build();
        personRepo.save(person);

        PersonId personId = PersonId.builder()
                .naturalId(Phaker.validNric())
                .personIdType(PersonIdTypeEnum.NRIC)
                .person(person)
                .build();
        personIdrepo.save(personId);

        return personId;
    }

    public PersonId createDualCitizen() {
        Batch batch = Batch.builder().build();
        Person person = Person.builder()
                .restricted(RestrictedEnum.NORMAL)
                .build();
        Nationality nationality = Nationality.builder()
                .nationality(NationalityEnum.DUAL_CITIZENSHIP)
                .batch(batch)
                .person(person)
                .build();

        PersonId personId = PersonId.builder()
                .naturalId(Phaker.validNric())
                .person(person)
                .personIdType(PersonIdTypeEnum.NRIC)
                .build();

        batchRepo.save(batch);
        personRepo.save(person);
        nationalityRepo.save(nationality);
        personIdrepo.save(personId);

        return personId;
    }
}
