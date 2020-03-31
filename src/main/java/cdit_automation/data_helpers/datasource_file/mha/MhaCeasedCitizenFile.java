package cdit_automation.data_helpers.datasource_file.mha;

import cdit_automation.data_helpers.datasource_file.mha.DataFields.BodyContent.MhaBodyDataField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.BodyContent.MhaCeasedCitizenBodyDataField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.DateField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.DateOfDeathField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.FooterOfBodyCountField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.NricOrFinField;
import cdit_automation.data_setup.Phaker;
import lombok.Builder;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class MhaCeasedCitizenFile extends MhaFile {
    public MhaCeasedCitizenFile() {
        super();
    }
    public MhaCeasedCitizenFile(int bodyCount) {
        super(bodyCount);
    }

    @Override
    protected void init(int rowCount) {
        super.fileName = "mha_ceased_citizen_e2e_file.txt";
        this.header = DateField.builder()
                .value(LocalDate.now().format(Phaker.DATETIME_FORMATTER_YYYYMMDD))
                .build();
        this.body = Collections.nCopies(rowCount, 0).stream().map(i-> new MhaCeasedCitizenBodyDataField()).collect(Collectors.toList());
        this.footer = new FooterOfBodyCountField(this.body.size());
    }
}