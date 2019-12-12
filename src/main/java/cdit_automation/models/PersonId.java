package cdit_automation.models;

import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.PersonIdTypeEnum;
import cdit_automation.models.embeddables.BiTemporalData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Check;

import javax.annotation.Nullable;
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

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode
@Check(constraints = "person_id_type IN ('NRIC', 'FIN', 'PP')")
@Table(name = "person_id")
public class PersonId extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "entity_key")
    @NotNull
    private Person person;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "person_id_type")
    private PersonIdTypeEnum personIdType;

    @NotNull
    @Column(name = "natural_id")
    private String naturalId;

    @JsonIgnore
    @Embedded
    private BiTemporalData biTemporalData;

    public static PersonId create(@NotNull PersonIdTypeEnum personIdTypeEnum, @NotNull Person person, @NotNull BiTemporalData biTemporalData) {
        String naturalId = "";
        switch(personIdTypeEnum) {
            case FIN:
                naturalId = Phaker.validFin();
                break;
            case PP:
            case NRIC:
            default:
                naturalId = Phaker.validNric();
        }
        return build(personIdTypeEnum, person, naturalId, biTemporalData);
    }

    public static PersonId create(@NotNull PersonIdTypeEnum personIdTypeEnum, @NotNull Person person, @Nullable String naturalId, @NotNull BiTemporalData biTemporalData) {
        if ( naturalId == null || naturalId.isEmpty() ) {
            switch(personIdTypeEnum) {
                case FIN:
                    naturalId = Phaker.validFin();
                    break;
                case PP:
                case NRIC:
                default:
                    naturalId = Phaker.validNric();
            }
        }
        return build(personIdTypeEnum, person, naturalId, biTemporalData);
    }

    private static PersonId build(@NotNull PersonIdTypeEnum personIdTypeEnum, @NotNull Person person, @Nullable String naturalId, @NotNull BiTemporalData biTemporalData) {
        return PersonId.builder()
                .personIdType(personIdTypeEnum)
                .person(person)
                .naturalId(naturalId)
                .biTemporalData(biTemporalData)
                .build();
    }
}