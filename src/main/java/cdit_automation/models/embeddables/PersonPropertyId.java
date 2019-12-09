package cdit_automation.models.embeddables;

import cdit_automation.models.Person;
import cdit_automation.models.Property;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonPropertyId implements Serializable {
    @ManyToOne
    @JoinColumn(name = "entity_key", nullable = false)
    @NotNull
    private Person personEntity;

    @ManyToOne
    @JoinColumn(name = "property_entity_key", nullable = false)
    @NotNull
    private Property propertyEntity;
}
