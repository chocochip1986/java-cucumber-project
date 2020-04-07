package cdit_automation.data_helpers.datasource_file.mha.TestDataStruct;

import cdit_automation.enums.datasource.FileTypeEnum;
import com.google.common.base.Joiner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.assertj.core.util.Strings;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class G1224TestData {
    public String nric;
    public String fin;
    public String dateOfBirth;
    public String name;
    public String attainmentDate;
    public String ceasedDate;
    public String fileName;
    public String validFrom;
    public String validTill;
    public String nationality;
    public FileTypeEnum fileType;

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }

        if(!(o instanceof G1224TestData)) {
            return false;
        }

        final G1224TestData input = (G1224TestData) o;

        if (!compareFieldEqual(this.nric, input.nric)) {
            return false;
        }

        if (!compareFieldEqual(this.fin, input.fin)) {
            return false;
        }
        if (!compareFieldEqual(this.dateOfBirth, input.dateOfBirth)) {
            return false;
        }
        if (!compareFieldEqual(this.name, input.name)) {
            return false;
        }
        if (!compareFieldEqual(this.attainmentDate, input.attainmentDate)) {
            return false;
        }
        if (!compareFieldEqual(this.ceasedDate, input.ceasedDate)) {
            return false;
        }
        if (!compareFieldEqual(this.fileName, input.fileName)) {
            return false;
        }
        if (!compareFieldEqual(this.validFrom, input.validFrom)) {
            return false;
        }
        if (!compareFieldEqual(this.validTill, input.validTill)) {
            return false;
        }
        if (!compareFieldEqual(this.nationality, input.nationality)) {
            return false;
        }

        return true;
    }

    private boolean compareFieldEqual(String a, String b) {
        if (a == null && b == null) {
            return true;
        }
        if(a != null && b != null) {
            return a.equals(b);
        }
        return false;
    }

    @Override
    public String toString() {
        return Joiner.on(", ").join(
                Strings.isNullOrEmpty(this.nric) ? "" : this.nric,
                Strings.isNullOrEmpty(this.fin) ? "": this.fin,
                Strings.isNullOrEmpty(this.dateOfBirth) ? "": this.dateOfBirth,
                Strings.isNullOrEmpty(this.name) ? "": this.name,
                Strings.isNullOrEmpty(this.attainmentDate) ? "": this.attainmentDate,
                Strings.isNullOrEmpty(this.ceasedDate) ? "": this.ceasedDate,
                Strings.isNullOrEmpty(this.fileName) ? "": this.fileName,
                Strings.isNullOrEmpty(this.validFrom) ? "": this.validFrom,
                Strings.isNullOrEmpty(this.validTill) ? "": this.validTill,
                Strings.isNullOrEmpty(this.nationality) ? "": this.nationality,
                this.fileType == null ? "": this.fileType.getValue()
        );
    }
}