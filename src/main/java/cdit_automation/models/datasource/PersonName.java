package cdit_automation.models.datasource;

import cdit_automation.data_setup.Phaker;
import cdit_automation.models.datasource.embeddables.BiTemporalData;
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

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "person_name")
@SuperBuilder
public class PersonName extends AbstractEntity {
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

    @NotNull
    @Column(name = "name")
    private String name;

    @Embedded
    private BiTemporalData biTemporalData;

    public static PersonName create(Batch batch, Person person, BiTemporalData biTemporalData) {
        return create(batch, person, Phaker.validName(), biTemporalData);
    }

    public static PersonName create(Batch batch, Person person, String name, BiTemporalData biTemporalData) {
        return PersonName.builder()
                .batch(batch)
                .person(person)
                .name(name)
                .biTemporalData(biTemporalData)
                .build();
    }
}
