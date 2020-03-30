package cdit_automation.data_helpers.datasource_file.mha;

import cdit_automation.data_helpers.datasource_file.mha.DataFields.BodyContent.MhaDeathBroadcastBodyDataField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.DateField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.FooterOfBodyCountField;
import cdit_automation.data_setup.Phaker;
import lombok.Builder;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.stream.Collectors;

public class MhaDeathBroadcastFile extends MhaFile {
    public MhaDeathBroadcastFile() {
        super();
    }
    public MhaDeathBroadcastFile(int bodyCount) {
        super(bodyCount);
    }

    @Override
    protected void init(int rowCount) {
        super.fileName = "mha_death_date_e2e_file.txt";
        this.header = DateField.builder()
                .value(LocalDate.now().format(Phaker.DATETIME_FORMATTER_YYYYMMDD))
                .build();
        this.body = Collections.nCopies(rowCount, 0).stream().map(i -> new MhaDeathBroadcastBodyDataField()).collect(Collectors.toList());
        this.footer = new FooterOfBodyCountField(this.body.size());
    }
}
