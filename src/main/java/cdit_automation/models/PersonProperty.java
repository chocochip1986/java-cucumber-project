package cdit_automation.models;

import cdit_automation.enums.PersonPropertyTypeEnum;
import cdit_automation.models.embeddables.BiTemporalData;
import cdit_automation.models.embeddables.PersonPropertyId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode
@Table(name = "person_property")
public class PersonProperty extends AbstractEntity {
    @EmbeddedId
    private PersonPropertyId identifier;

    @ManyToOne
    @JoinColumn(name = "batch_id")
    @NotNull
    private Batch batch;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    @NotNull
    private PersonPropertyTypeEnum type;

    @JsonIgnore
    @Embedded
    private BiTemporalData biTemporalData;
}
