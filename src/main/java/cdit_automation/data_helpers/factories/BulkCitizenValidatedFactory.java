package cdit_automation.data_helpers.factories;


import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.AddressIndicatorEnum;
import cdit_automation.enums.NationalityEnum;
import cdit_automation.enums.YesNoTypeEnum;
import cdit_automation.models.Batch;
import cdit_automation.models.BulkCitizenValidated;
import cdit_automation.models.BulkMhaAddressValidated;
import cdit_automation.models.BulkNcaAddressValidated;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Random;

@Component
public class BulkCitizenValidatedFactory extends AbstractFactory {

    public BulkCitizenValidated createValidBulkCitizenValidatedRecord(Batch batch) {
        BulkCitizenValidated bulkCitizenValidated;
        int[] arrayOfChoices = new int[]{1,2,3};
        switch(arrayOfChoices[new Random().nextInt(arrayOfChoices.length)]) {
            case 1:
                bulkCitizenValidated = createWithMHAAddressZ(batch);
                break;
            case 2:
                bulkCitizenValidated = createWithMHAAddressC(batch);
                break;
            case 3:
                bulkCitizenValidated = createWithNCAAddress(batch);
                break;
            default:
                bulkCitizenValidated = createWithMHAAddressZ(batch);
        }
        bulkCitizenValidatedRepo.save(bulkCitizenValidated);
        return bulkCitizenValidated;
    }

    public BulkCitizenValidated createWithMHAAddressZ(Batch batch) {
        LocalDate birthDate = Phaker.validDate(LocalDate.now().minusYears(12), LocalDate.now());
        BulkCitizenValidated result =
                BulkCitizenValidated.builder()
                        .batch(batch)
                        .nric(Phaker.validNric())
                        .name(Phaker.validName())
                        .dateOfBirth(birthDate)
                        .dateOfdeath(null)
                        .gender(Phaker.validGender())
                        .addressIndicator(AddressIndicatorEnum.MHA_Z)
                        .nationality(NationalityEnum.SINGAPORE_CITIZEN)
                        .citizenshipAttainmentIssueDate(birthDate)
                        .dualCitizenship(YesNoTypeEnum.random())
                        .build();


        result.setBulkMhaAddressValidated(BulkMhaAddressValidated.create());
        return result;
    }

    public BulkCitizenValidated createWithMHAAddressC(Batch batch) {
        LocalDate birthDate = Phaker.validDate(LocalDate.now().minusYears(12), LocalDate.now());
        BulkCitizenValidated result =
                BulkCitizenValidated.builder()
                        .batch(batch)
                        .nric(Phaker.validNric())
                        .name(Phaker.validName())
                        .dateOfBirth(birthDate)
                        .dateOfdeath(null)
                        .gender(Phaker.validGender())
                        .addressIndicator(AddressIndicatorEnum.MHA_C)
                        .nationality(NationalityEnum.SINGAPORE_CITIZEN)
                        .citizenshipAttainmentIssueDate(birthDate)
                        .dualCitizenship(YesNoTypeEnum.random())
                        .build();


        result.setBulkMhaAddressValidated(BulkMhaAddressValidated.createOverseas());
        return result;
    }

    public BulkCitizenValidated createWithNCAAddress(Batch batch) {
        LocalDate birthDate = Phaker.validDate(LocalDate.now().minusYears(12), LocalDate.now());
        BulkCitizenValidated result =
                BulkCitizenValidated.builder()
                        .batch(batch)
                        .nric(Phaker.validNric())
                        .name(Phaker.validName())
                        .dateOfBirth(birthDate)
                        .dateOfdeath(null)
                        .gender(Phaker.validGender())
                        .addressIndicator(AddressIndicatorEnum.MHA_C)
                        .nationality(NationalityEnum.SINGAPORE_CITIZEN)
                        .citizenshipAttainmentIssueDate(birthDate)
                        .dualCitizenship(YesNoTypeEnum.random())
                        .build();


        result.setBulkNcaAddressValidated(BulkNcaAddressValidated.create());
        return result;
    }
}
