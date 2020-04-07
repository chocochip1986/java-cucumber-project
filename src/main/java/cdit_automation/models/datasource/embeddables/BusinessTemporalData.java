package cdit_automation.models.datasource.embeddables;

import cdit_automation.constants.Constants;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;

@Getter
@Embeddable
@Builder
public class BusinessTemporalData implements Serializable {
    @Column(name = "valid_from")
    private final Timestamp validFrom;

    @Column(name = "valid_till")
    private final Timestamp validTill;

    public BusinessTemporalData() {
        this(null, null);
    }

    public BusinessTemporalData(Timestamp validFrom) {
        this(validFrom, Timestamp.valueOf(Constants.INFINITE_LOCAL_DATE_TIME));
    }

    public BusinessTemporalData(Timestamp validFrom, Timestamp validTill) {
        this.validFrom = validFrom;
        this.validTill = validTill;
    }

    public BusinessTemporalData newValidFrom(Timestamp validFrom) {
        return new BusinessTemporalData(validFrom, this.validTill);
    }

    public BusinessTemporalData newValidTill(Timestamp validTill) {
        return new BusinessTemporalData(this.validFrom, validTill);
    }
}
