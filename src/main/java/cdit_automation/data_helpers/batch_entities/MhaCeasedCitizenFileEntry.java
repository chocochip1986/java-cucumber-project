package cdit_automation.data_helpers.batch_entities;

import cdit_automation.utilities.StringUtils;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MhaCeasedCitizenFileEntry {
    private String nric;
    private String name;
    private String nationality;
    private LocalDate citizenRenunciationDate;

    public String toString() {
        return StringUtils.rightPad(this.nric, 9)
                + StringUtils.rightPad(this.name, 66)
                + StringUtils.rightPad(this.nationality, 2)
                + StringUtils.rightPad(this.citizenRenunciationDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")), 8);
    }
}
