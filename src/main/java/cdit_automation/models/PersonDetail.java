package cdit_automation.models;

import cdit_automation.data_setup.Phaker;
import cdit_automation.models.embeddables.BiTemporalData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
import org.hibernate.annotations.Check;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode
@Table(name = "person_detail")
@Check(constraints = "gender IN ('MALE', 'FEMALE', 'UNKNOWN')")
public class PersonDetail {
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

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "date_of_death")
    private LocalDate dateOfDeath;

    @JsonIgnore
    @Embedded
    private BiTemporalData biTemporalData;

    public static PersonDetail create(@NotNull Batch batch, @NotNull Person person, BiTemporalData biTemporalData) {
        return create(batch, person, Phaker.validPastDate(), null, biTemporalData);
    }

    public static PersonDetail create(Batch batch, Person person, LocalDate birthDate, LocalDate deathDate, BiTemporalData biTemporalData) {
        return PersonDetail.builder()
                .batch(batch)
                .person(person)
                .dateOfBirth(birthDate)
                .dateOfDeath(deathDate)
                .biTemporalData(biTemporalData)
                .build();
    }

}
