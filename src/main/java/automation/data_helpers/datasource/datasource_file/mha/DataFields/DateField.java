package automation.data_helpers.datasource.datasource_file.mha.DataFields;

import automation.data_helpers.datasource.datasource_file.DataField;
import automation.data_setup.Phaker;
import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@SuperBuilder
@Getter
@Setter
public class DateField extends DataField {

    public DateField() {
        this.value = Phaker.validPastDate().format(Phaker.DATETIME_FORMATTER_YYYYMMDD);
    }

    public DateField(boolean isAfter, boolean allowOn, LocalDate date) {
        this.value = randomDateWithConstraints(isAfter, allowOn, date).format(Phaker.DATETIME_FORMATTER_YYYYMMDD);
    }

    @Override
    public String name() {
        return "date";
    }

    @Override
    public int length() {
        return 8;
    }

    @Override
    public String toRawString() {
        return Strings.padEnd(this.value, this.length(), ' ');
    }

    protected LocalDate randomDateWithConstraints(boolean isAfter, boolean allowOn, LocalDate date) {
        if(allowOn && faker.random().nextInt(49) % 7 == 0) {
            return date;
        }
        if(isAfter) {
            return date.plusYears(faker.random().nextInt(90)).plusMonths(faker.random().nextInt(12)).plusDays(faker.random().nextInt(31));
        }
        else {
            return date.minusYears(faker.random().nextInt(90)).minusMonths(faker.random().nextInt(12)).minusDays(faker.random().nextInt(31));
        }
    }
}
