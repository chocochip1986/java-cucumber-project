package cdit_automation.models;

import java.io.Serializable;
import javax.persistence.MappedSuperclass;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@SuperBuilder
public abstract class AbstractEntity implements Serializable {

    public AbstractEntity () {

    }
}
