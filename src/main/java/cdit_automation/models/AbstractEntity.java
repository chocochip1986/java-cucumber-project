package cdit_automation.models;

import lombok.experimental.SuperBuilder;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
@SuperBuilder
public abstract class AbstractEntity implements Serializable {

    public AbstractEntity () {

    }
}
