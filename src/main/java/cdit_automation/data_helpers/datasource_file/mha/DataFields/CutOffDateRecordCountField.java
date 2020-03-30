package cdit_automation.data_helpers.datasource_file.mha.DataFields;

import cdit_automation.data_helpers.datasource_file.DataField;
import cdit_automation.data_setup.Phaker;
import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@SuperBuilder
@Getter
@Setter
public class CutOffDateRecordCountField extends DataField {

    public CutOffDateRecordCountField() {
        this.value = LocalDate.now().format(Phaker.DATETIME_FORMATTER_YYYYMMDD) +
                Strings.padStart(faker.random().nextInt(10000)+"", 4, '0');
    }

    public CutOffDateRecordCountField(LocalDate cutOffDate, int recordCount) {
        this.value = cutOffDate.format(Phaker.DATETIME_FORMATTER_YYYYMMDD) + Strings.padStart(recordCount + "", 4, '0');
    }

    public CutOffDateRecordCountField(int recordCount) {
        this.value = LocalDate.now().format(Phaker.DATETIME_FORMATTER_YYYYMMDD) + Strings.padStart(recordCount + "", 4, '0');
    }

    @Override
    public String name() {
        return "cutOffDateRecordCount";
    }

    @Override
    public int length() {
        return 12;
    }

    @Override
    public String toRawString() {
        return Strings.padEnd(this.value, this.length(), ' ');
    }
}
