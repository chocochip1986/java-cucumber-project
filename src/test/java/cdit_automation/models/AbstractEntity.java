package cdit_automation.models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
@SuperBuilder
public abstract class AbstractEntity implements Serializable {

    public AbstractEntity () {

    }
}
