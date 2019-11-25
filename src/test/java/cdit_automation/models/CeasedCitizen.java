package cdit_automation.models;

import cdit_automation.enums.CeasedCitizenNationalityEnum;
import cdit_automation.enums.CeasedCitizenNricCancelledStatusEnum;
import cdit_automation.utilities.StringUtils;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CeasedCitizen {

    public String nric;
    public String name;
    public CeasedCitizenNationalityEnum nationality;
    public String citizenRenunciationDate;
    public CeasedCitizenNricCancelledStatusEnum nricCancelledStatus;

    @Override
    public String toString() {
        return StringUtils.rightPad(this.nric, 9)
                + StringUtils.rightPad(this.name, 66)
                + StringUtils.rightPad(this.nationality.getValue(), 2)
                + StringUtils.rightPad(this.citizenRenunciationDate, 8)
                + StringUtils.rightPad(this.nricCancelledStatus.getValue(), 1);
    }
}
