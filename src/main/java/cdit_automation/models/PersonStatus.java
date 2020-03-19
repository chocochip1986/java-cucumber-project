package cdit_automation.models;

import cdit_automation.enums.PersonStatusTypeEnum;
import cdit_automation.models.embeddables.BiTemporalData;
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
@Check(constraints = "type IN ('MERDEKA_GENERATION_PACKAGE'," + "'NO_INTERACTION')")
@Table(name = "person_status")
public class PersonStatus {
    
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

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    @NotNull
    private PersonStatusTypeEnum type;

    @JsonIgnore
    @Embedded
    private BiTemporalData biTemporalData;
}
