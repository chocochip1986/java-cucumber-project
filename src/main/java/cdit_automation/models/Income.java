package cdit_automation.models;

import cdit_automation.enums.AssessableIncomeStatus;
import cdit_automation.models.embeddables.BiTemporalData;
import java.math.BigDecimal;
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
@Table(name = "income")
@Builder
public class Income extends AbstractEntity {
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
  @Column(name = "year")
  private Short year;

  @Column(name = "assessable_income", columnDefinition = "NUMBER(11,4)")
  private BigDecimal assessableIncome;

  @NotNull
  @Column(name = "assessable_income_status")
  @Enumerated(EnumType.STRING)
  private AssessableIncomeStatus assessableIncomeStatus;

  @NotNull
  @Column(name = "appeal_case")
  private boolean isAppealCase;

  @Embedded private BiTemporalData biTemporalData;

  public static Income create(
      @NotNull Batch batch,
      @NotNull Person person,
      @NotNull Short year,
      BigDecimal assessableIncome,
      @NotNull AssessableIncomeStatus assessableIncomeStatus,
      BiTemporalData biTemporalData) {
    return Income.builder()
        .batch(batch)
        .person(person)
        .year(year)
        .assessableIncome(assessableIncome)
        .assessableIncomeStatus(assessableIncomeStatus)
        .biTemporalData(biTemporalData)
        .build();
  }
}
