package cdit_automation.models.datasource;

import cdit_automation.constants.datasource.ErrorMessageConstants;
import cdit_automation.enums.datasource.ResultIndicatorEnum;
import cdit_automation.validator.IsUpperCase;
import cdit_automation.validator.ValidEnumType;
import cdit_automation.validator.ValidID;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "assessable_income_validated")
public class AssessableIncomeValidated extends AbstractValidated {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "batch_id")
  @NotNull
  private Batch batch;

  @Column(name = "record_type")
  @NotNull
  private int recordType;

  @Column(name = "id_number")
  @NotNull
  @IsUpperCase
  @ValidID
  private String idNumber;

  @Column(name = "assessable_income")
  @NotNull
  private BigDecimal assessableIncome;

  @Column(name = "record_details_validated")
  private String recordDetailsValidated;

  @Column(name = "is_duplicate_validated")
  private Long isDuplicateValidated;

  @Column(name = "result_indicator")
  @NotNull
  @ValidEnumType(
      enumClazz = ResultIndicatorEnum.class,
      message = ErrorMessageConstants.INVALID_RESULT_INDICATOR)
  @Enumerated(EnumType.STRING)
  private ResultIndicatorEnum resultIndicator;

  @Column(name = "assessment_year")
  @NotNull
  private int yearOfAssessment;

  @Column(name = "is_mappable")
  @NotNull
  private Boolean isMappable;
}
