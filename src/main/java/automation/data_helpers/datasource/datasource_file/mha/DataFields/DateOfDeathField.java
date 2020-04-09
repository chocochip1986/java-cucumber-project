package automation.data_helpers.datasource.datasource_file.mha.DataFields;

import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@SuperBuilder
public class DateOfDeathField extends DateOfBirthField {
    public DateOfDeathField() {
        super();
    }

    public DateOfDeathField(boolean isAfter, boolean allowOn, LocalDate date) {
        super(isAfter, allowOn, date);
    }

    @Override
    public String name() {
        return "dateOfDeath";
    }
}
