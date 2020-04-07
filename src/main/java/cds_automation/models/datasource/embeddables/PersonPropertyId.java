package cds_automation.models.datasource.embeddables;

import cds_automation.enums.datasource.PersonPropertyTypeEnum;
import cds_automation.models.datasource.Person;
import cds_automation.models.datasource.Property;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    @NotNull
    private PersonPropertyTypeEnum type;

    @Column(name = "valid_from")
    @NotNull
    private Timestamp validFrom;
}
