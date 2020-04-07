package cds_automation.data_helpers.datasource.datasource_file.mha.DataFields.NcaAddress;

import cds_automation.data_helpers.datasource.datasource_file.DataField;
import com.google.common.base.Strings;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.RandomStringUtils;

@SuperBuilder
public class NcaStreetCodeField extends DataField {
    public NcaStreetCodeField() {
        this.value = RandomStringUtils.randomAlphabetic(this.length());
    }

    @Override
    public String name() {
        return "ncaStreetCode";
    }

    @Override
    public int length() {
        return 6;
    }

    @Override
    public String toRawString() {
        return Strings.padEnd(this.value, this.length(), ' ');
    }
}
