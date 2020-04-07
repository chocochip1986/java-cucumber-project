package cds_automation.models.datasource;

import cds_automation.models.datasource.embeddables.DbTemporalData;
import cds_automation.models.datasource.embeddables.PersonPropertyId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;


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

    @Embedded @JsonIgnore protected DbTemporalData dbTemporalData;

    @Column(name = "valid_till")
    private Timestamp validTill;


//    @JsonIgnore
//    @Embedded
//    private BiTemporalData biTemporalData;

    public static PersonProperty createPersonProperty(Batch batch, PersonPropertyId personPropertyId, Timestamp validTill) {
        return build(batch, personPropertyId, validTill);
    }


    public static PersonProperty create(Batch batch, PersonPropertyId personPropertyId, Timestamp validTill) {
        return build(batch, personPropertyId, validTill);
    }

    private static PersonProperty build(Batch batch, PersonPropertyId personPropertyId, Timestamp validTill) {
        return PersonProperty.builder()
                .batch(batch)
                .dbTemporalData(new DbTemporalData())
                .validTill(validTill)
                .identifier(personPropertyId)
//                .type(personPropertyTypeEnum)
                .build();
    }
}
