package automation.data_helpers.datasource.datasource_file.mha;

import automation.data_helpers.datasource.datasource_file.mha.DataFields.BodyContent.MhaNewCitizenBodyDataField;
import automation.data_helpers.datasource.datasource_file.mha.DataFields.DateField;
import automation.data_helpers.datasource.datasource_file.mha.DataFields.FooterOfBodyCountField;
import automation.data_setup.Phaker;
import lombok.Builder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.stream.Collectors;

@Builder
public class MhaNewCitizenFile extends MhaFile {
    public MhaNewCitizenFile() {
        super();
    }
    public MhaNewCitizenFile(int bodyCount) {
        super(bodyCount);
    }

    @Override
    protected void init(int rowCount) {
        super.fileName = "mha_new_citizen_e2e_file.txt";
        this.header = DateField.builder()
                .value(LocalDate.now().format(Phaker.DATETIME_FORMATTER_YYYYMMDD))
                .build();
        this.body = Collections.nCopies(rowCount, 0).stream().map(i-> new MhaNewCitizenBodyDataField()).collect(Collectors.toList());
        this.footer = new FooterOfBodyCountField(this.body.size());
    }
}
