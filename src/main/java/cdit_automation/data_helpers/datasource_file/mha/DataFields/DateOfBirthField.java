package cdit_automation.data_helpers.datasource_file.mha.DataFields;

import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@SuperBuilder
public class DateOfBirthField extends DateField {

    public DateOfBirthField() {
        super();
    }

    public DateOfBirthField(boolean isAfter, boolean allowOn, LocalDate date) {
        super(isAfter, allowOn, date);
    }

    @Override
    public String name() {
        return "dateOfBirth";
    }
}
