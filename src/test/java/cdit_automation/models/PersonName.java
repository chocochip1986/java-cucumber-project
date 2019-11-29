package cdit_automation.models;

import cdit_automation.models.embeddables.BiTemporalData;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

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
    @JoinColumn(name = "entity_key", nullable = false)
    @NotNull
    private Person person;

    @NotNull
    @Column(name = "name")
    private String name;

    @Embedded
    private BiTemporalData biTemporalData;
}
