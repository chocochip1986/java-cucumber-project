package cds_automation.data_helpers.datasource.batch_entities;

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
public class MhaNoInteractionFileEntry {
    
    private String nric;
    private String validFromDate;
    private String validTillDate;

    public String toString() {
        return this.nric + this.validFromDate + this.validTillDate;
    }
}
