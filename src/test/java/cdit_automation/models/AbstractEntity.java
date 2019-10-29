package cdit_automation.models;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;

@MappedSuperclass
public abstract class AbstractEntity {
    @Column(name = "entered")
    private Timestamp entered;

    @Column(name = "valid_from")
    private Timestamp validFrom;

    @Column(name = "valid_till")
    private Timestamp validTill;
}
