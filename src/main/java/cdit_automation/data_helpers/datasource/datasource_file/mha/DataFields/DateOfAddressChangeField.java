package cdit_automation.data_helpers.datasource.datasource_file.mha.DataFields;

import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@SuperBuilder
public class DateOfAddressChangeField extends DateOfBirthField {
    public DateOfAddressChangeField() {
        super();
    }

    public DateOfAddressChangeField(boolean isAfter, boolean allowOn, LocalDate date) {
        super(isAfter, allowOn, date);
    }

    @Override
    public String name() {
        return "dateOfAddressChange";
    }
}
