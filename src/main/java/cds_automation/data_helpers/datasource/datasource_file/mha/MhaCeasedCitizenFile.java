package cds_automation.data_helpers.datasource.datasource_file.mha;

import cds_automation.data_helpers.datasource.datasource_file.mha.DataFields.BodyContent.MhaCeasedCitizenBodyDataField;
import cds_automation.data_helpers.datasource.datasource_file.mha.DataFields.DateField;
import cds_automation.data_helpers.datasource.datasource_file.mha.DataFields.FooterOfBodyCountField;
import cds_automation.data_setup.Phaker;

import java.time.LocalDate;
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
