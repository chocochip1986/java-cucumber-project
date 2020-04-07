package cdit_automation.data_helpers.datasource.datasource_file.mha;

import cdit_automation.data_helpers.datasource.datasource_file.mha.DataFields.BodyContent.MhaPersonDetailChangeBodyDataField;
import cdit_automation.data_helpers.datasource.datasource_file.mha.DataFields.DateField;
import cdit_automation.data_helpers.datasource.datasource_file.mha.DataFields.FooterOfBodyCountField;
import cdit_automation.data_setup.Phaker;

import java.time.LocalDate;
import java.util.Collections;
import java.util.stream.Collectors;

public class MhaPersonDetailChangeFile extends MhaFile {
    public MhaPersonDetailChangeFile() {
        super();
    }
    public MhaPersonDetailChangeFile(int bodyCount) {
        super(bodyCount);
    }

    @Override
    protected void init(int rowCount) {
        super.fileName = "mha_person_detail_change_e2e_file.txt";
        this.header = DateField.builder()
                .value(LocalDate.now().format(Phaker.DATETIME_FORMATTER_YYYYMMDD))
                .build();
        this.body = Collections.nCopies(rowCount, 0).stream().map(i-> new MhaPersonDetailChangeBodyDataField()).collect(Collectors.toList());
        this.footer = new FooterOfBodyCountField(this.body.size());
    }
}
