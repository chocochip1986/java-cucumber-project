package cds_automation.data_helpers.datasource.datasource_file.mha;

import cds_automation.data_helpers.datasource.datasource_file.mha.DataFields.BodyContent.MhaChangeAddressBodyDataField;
import cds_automation.data_helpers.datasource.datasource_file.mha.DataFields.CutOffDateRecordCountField;
import lombok.Builder;

import java.util.Collections;
import java.util.stream.Collectors;

@Builder
public class MhaChangeAddressFile extends MhaFile {
    public MhaChangeAddressFile() {
        super();
    }
    public MhaChangeAddressFile(int bodyCount) {
        super(bodyCount);
    }

    @Override
    protected void init(int rowCount) {
        super.fileName = "mha_change_address_e2e_file.txt";
        this.header = new CutOffDateRecordCountField(rowCount);
        this.body = Collections.nCopies(rowCount, 0).stream().map(i-> new MhaChangeAddressBodyDataField()).collect(Collectors.toList());
        this.footer = null;
    }

    @Override
    public String toRawString() {
        return new StringBuilder()
                .append(header.toRawString())
                .append(body.stream().map(i -> i.toRawString()).reduce("", (partialString, element) -> partialString + "\n" + element)).toString();
    }
}
