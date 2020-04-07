package cdit_automation.data_helpers.datasource.datasource_file.mha.DataFields.MhaAddress;

import cdit_automation.data_helpers.datasource.datasource_file.DataField;
import com.google.common.base.Strings;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class MhaBuildingNameField extends DataField {
    public MhaBuildingNameField() {
        final String buildingName = faker.space().agency();
        this.value = buildingName.substring(0, Math.min(this.length(), buildingName.length()));
    }

    @Override
    public String name() {
        return "mhaBuildingName";
    }

    @Override
    public int length() {
        return 30;
    }

    @Override
    public String toRawString() {
        return Strings.padEnd(this.value, this.length(), ' ');
    }
}
