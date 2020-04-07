package cds_automation.models.datasource;

import cds_automation.enums.datasource.GenderEnum;
import cds_automation.models.datasource.embeddables.BiTemporalData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Table(name = "gender")
public class Gender extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "batch_id")
    @NotNull
    private Batch batch;

    @ManyToOne
    @JoinColumn(name = "entity_key")
    @NotNull
    private Person person;

    @SuppressWarnings("squid:S1700")
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private GenderEnum genderEnum;

    @JsonIgnore
    @Embedded
    private BiTemporalData biTemporalData;

    public static Gender create(GenderEnum genderEnum, Person person, Batch batch, BiTemporalData biTemporalData) {
        return build(genderEnum, person, batch, biTemporalData);
    }

    private static Gender build(GenderEnum genderEnum, Person person, Batch batch, BiTemporalData biTemporalData) {
        return Gender.builder().genderEnum(genderEnum).person(person).batch(batch).biTemporalData(biTemporalData).build();
    }
}
