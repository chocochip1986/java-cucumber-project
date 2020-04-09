package automation.models.datasource.embeddables;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Embeddable
@EqualsAndHashCode
public final class DbTemporalData implements Serializable {
    @Column(name = "entered")
    private final Timestamp entered;

    public DbTemporalData() {
        this.entered = Timestamp.valueOf(LocalDateTime.now());
    }

    public DbTemporalData(Timestamp entered) {
        this.entered = entered;
    }
}
