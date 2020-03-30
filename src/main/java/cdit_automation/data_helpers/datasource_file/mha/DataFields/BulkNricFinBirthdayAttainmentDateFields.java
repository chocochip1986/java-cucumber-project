package cdit_automation.data_helpers.datasource_file.mha.DataFields;

import cdit_automation.data_helpers.datasource_file.DataField;
import cdit_automation.data_setup.Phaker;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@SuperBuilder
@AllArgsConstructor
public class BulkNricFinBirthdayAttainmentDateFields extends DataField {

    public NricField nric;
    public FinField fin;
    public CitizenshipAttainmentDateField attainmentDate;
    public DateOfBirthField dateOfBirth;
    public DateOfDeathField dateOfDeath;

    private final int MIN_AGE = 13;

    public BulkNricFinBirthdayAttainmentDateFields(String nric, String fin, String attainmentDate, String birthday, String deathDate) {
        this.nric = NricField.builder().value(nric).build();
        this.fin = FinField.builder().value(fin).build();
        this.attainmentDate = CitizenshipAttainmentDateField.builder().value(attainmentDate).build();
        this.dateOfBirth = DateOfBirthField.builder().value(birthday).build();
        this.dateOfDeath = DateOfDeathField.builder().value(deathDate).build();
    }


    public BulkNricFinBirthdayAttainmentDateFields(LocalDate dateOfRun) {
        init(dateOfRun);
    }

    /**
     * Needs to be at least 13 years of age, assume default date of run is today
     */
    public BulkNricFinBirthdayAttainmentDateFields() {
        init(LocalDate.now());
    }

    private void init(LocalDate dateOfRun) {
        this.nric = new NricField();

        final LocalDate birthdayDate = dateOfRun.minusYears(faker.random().nextInt(MIN_AGE, 98)).minusMonths(faker.random().nextInt(12)).minusDays(faker.random().nextInt(31));
        final LocalDate attainDate = dateOfRun.minusYears(faker.random().nextInt(10)).minusMonths(faker.random().nextInt(12)).minusDays(faker.random().nextInt(31));

        final String attainmentStr = attainDate.format(Phaker.DATETIME_FORMATTER_YYYYMMDD);
        final String birthdayStr = birthdayDate.format(Phaker.DATETIME_FORMATTER_YYYYMMDD);

        if (faker.random().nextBoolean()) {
            this.fin = new FinField();
            this.attainmentDate = CitizenshipAttainmentDateField.builder().value(attainmentStr).build();
            final String isBirthdaySameAsAttainment = faker.random().nextInt(49) % 3 == 0 ? attainmentStr : birthdayStr;
            this.dateOfBirth =  DateOfBirthField.builder().value(isBirthdaySameAsAttainment).build();
        }
        else {
            this.fin = FinField.builder().value("").build();
            if(faker.random().nextBoolean()) {
                this.attainmentDate = CitizenshipAttainmentDateField.builder().value(attainmentStr).build();

                final String isBirthdaySameAsAttainment = faker.random().nextInt(49) % 3 == 0 ? attainmentStr : birthdayStr;
                this.dateOfBirth =  DateOfBirthField.builder().value(isBirthdaySameAsAttainment).build();
            }
            else {
                this.attainmentDate = CitizenshipAttainmentDateField.builder().value("").build();
                this.dateOfBirth = DateOfBirthField.builder().value(birthdayStr).build();
            }
        }

        if(faker.random().nextBoolean()) {
            final LocalDate minDate = this.attainmentDate.value.isEmpty() ? birthdayDate : attainDate;
            this.dateOfDeath = DateOfDeathField.builder().value(Phaker.validDateFromRange(minDate, dateOfRun).format(Phaker.DATETIME_FORMATTER_YYYYMMDD)).build();
        }
        else {
            this.dateOfDeath = DateOfDeathField.builder().value("").build();
        }

    }

    @Override
    public String name() {
        return "nricFinBirthdayAttainmentDate";
    }

    @Override
    public int length() {
        return -1;
    }

    @Override
    public String toRawString() {
        return "";
    }
}
