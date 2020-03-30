package cdit_automation.data_helpers.datasource_file.mha.DataFields;

import cdit_automation.data_helpers.datasource_file.DataField;
import cdit_automation.data_setup.Phaker;
import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
public class NricFinAttainmentDateFields extends DataField {

    public NricField nric;
    public FinField fin;
    public CitizenshipAttainmentDateField attainmentDate;

    public NricFinAttainmentDateFields(String nric, String fin, String attainmentDate) {
        this.nric = NricField.builder().value(nric).build();
        this.fin = FinField.builder().value(fin).build();
        this.attainmentDate = CitizenshipAttainmentDateField.builder().value(attainmentDate).build();
    }

    public NricFinAttainmentDateFields() {
        this.nric = new NricField();
        if (faker.random().nextBoolean()) {
            this.fin = new FinField();
            this.attainmentDate = new CitizenshipAttainmentDateField();
        }
        else {
            this.fin = FinField.builder().value("").build();
            if(faker.random().nextBoolean()) {
                this.attainmentDate = new CitizenshipAttainmentDateField();
            }
            else {
                this.attainmentDate = CitizenshipAttainmentDateField.builder().value("").build();
            }
        }
    }

    @Override
    public String name() {
        return "nricFinAttainmentDate";
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
