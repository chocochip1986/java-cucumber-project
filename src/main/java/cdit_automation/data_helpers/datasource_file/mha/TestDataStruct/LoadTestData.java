package cdit_automation.data_helpers.datasource_file.mha.TestDataStruct;

import cdit_automation.enums.datasource.FileTypeEnum;
import com.google.common.base.Joiner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoadTestData {
    public String fileName;
    public FileTypeEnum fileType;
    public int count;

    @Override
    public String toString() {
        return Joiner.on(", ").join(
                this.fileName,
                this.fileType.getValue(),
                this.count
        );
    }
}