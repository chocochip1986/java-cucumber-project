package cdit_automation.models.embeddables;

import cdit_automation.constants.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Builder
public class BiTemporalData implements Serializable {
    @Embedded @JsonIgnore protected DbTemporalData dbTemporalData;

    @Embedded @JsonIgnore protected BusinessTemporalData businessTemporalData;

    public BiTemporalData generateNewBiTemporalData(Timestamp validfrom) {
        return BiTemporalData.builder()
                .businessTemporalData(
                        new BusinessTemporalData(
                                validfrom, Timestamp.valueOf(Constants.INFINITE_LOCAL_DATE_TIME)))
                .dbTemporalData(new DbTemporalData())
                .build();
    }

    public BiTemporalData generateNewBiTemporalData(Timestamp validfrom, Timestamp validtill) {
        return BiTemporalData.builder()
                .businessTemporalData(new BusinessTemporalData(validfrom, validtill))
                .dbTemporalData(new DbTemporalData())
                .build();
    }
}
