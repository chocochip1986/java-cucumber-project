package cdit_automation.models.interfaces;

import cdit_automation.enums.AssessableIncomeStatus;

import java.math.BigDecimal;

public interface ICustomIncomeRecord {
  String getNaturalId();

  BigDecimal getAssessableIncome();

  AssessableIncomeStatus getAssessableIncomeStatus();
}
