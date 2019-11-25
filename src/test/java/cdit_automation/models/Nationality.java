package cdit_automation.models;

import cdit_automation.enums.NationalityEnum;
import cdit_automation.models.embeddables.BiTemporalData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

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
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode
@Table(name = "nationality")
public class Nationality extends AbstractEntity {
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
    private NationalityEnum nationality;

    @Column(name = "citizenship_attainment_date")
    private Timestamp citizenshipAttainmentDate;

    @Column(name = "citizenship_renunciation_date")
    private Timestamp citizenshipRenunciationDate;

    @JsonIgnore
    @Embedded
    private BiTemporalData biTemporalData;
}