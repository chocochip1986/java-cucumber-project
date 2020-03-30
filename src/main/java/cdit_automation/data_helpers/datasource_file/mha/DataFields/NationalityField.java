package cdit_automation.data_helpers.datasource_file.mha.DataFields;

import cdit_automation.data_helpers.datasource_file.DataField;
import cdit_automation.utilities.StringUtils;
import com.google.common.base.Strings;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.RandomStringUtils;

@SuperBuilder
public class NationalityField extends DataField {
    public NationalityField() {
        String randNat = RandomStringUtils.randomAlphabetic(this.length());
        while (randNat.equalsIgnoreCase("SG")) {
            randNat = RandomStringUtils.randomAlphabetic(this.length());
        }
        this.value = randNat;
    }

    @Override
    public String name() {
        return "nationality";
    }

    @Override
    public int length() {
        return 2;
    }

    @Override
    public String toRawString() {
        return Strings.padEnd(this.value, this.length(), ' ');
    }
}
