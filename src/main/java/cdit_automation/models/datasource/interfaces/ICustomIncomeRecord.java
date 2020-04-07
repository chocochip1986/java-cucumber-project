package cdit_automation.models.datasource.interfaces;

import cdit_automation.enums.datasource.AssessableIncomeStatus;

import java.math.BigDecimal;

public interface ICustomIncomeRecord {
  String getNaturalId();

  BigDecimal getAssessableIncome();

  AssessableIncomeStatus getAssessableIncomeStatus();
}
