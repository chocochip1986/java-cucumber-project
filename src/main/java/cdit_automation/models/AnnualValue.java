package cdit_automation.models;

import cdit_automation.enums.AnnualValueStatus;
import cdit_automation.models.embeddables.BiTemporalData;
import java.math.BigDecimal;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "annual_value")
@Builder
public class AnnualValue extends AbstractEntity {
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
    private Property property;

    @SuppressWarnings("squid:S1700")
    @Nullable
    @Column(name = "annual_value", columnDefinition = "NUMBER(11,4)")
    private BigDecimal annualValue;

    @NotNull
    @Column(name = "annual_value_status")
    @Enumerated(EnumType.STRING)
    private AnnualValueStatus annualValueStatus;

    @Embedded
    private BiTemporalData biTemporalData;
}
