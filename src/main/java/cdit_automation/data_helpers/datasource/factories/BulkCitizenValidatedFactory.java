package cdit_automation.data_helpers.datasource.factories;


import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.datasource.AddressIndicatorEnum;
import cdit_automation.enums.datasource.NationalityEnum;
import cdit_automation.enums.datasource.YesNoTypeEnum;
import cdit_automation.models.datasource.Batch;
import cdit_automation.models.datasource.BulkCitizenValidated;
import cdit_automation.models.datasource.BulkMhaAddressValidated;
import cdit_automation.models.datasource.BulkNcaAddressValidated;
import java.time.LocalDate;
import java.util.Random;
import org.springframework.stereotype.Component;

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
