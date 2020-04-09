package automation.models.datasource.interfaces;

import automation.enums.datasource.AssessableIncomeStatus;

import java.math.BigDecimal;

public interface ICustomIncomeRecord {
  String getNaturalId();

  BigDecimal getAssessableIncome();

  AssessableIncomeStatus getAssessableIncomeStatus();
}
