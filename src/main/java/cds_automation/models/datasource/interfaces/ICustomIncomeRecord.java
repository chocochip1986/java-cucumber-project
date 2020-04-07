package cds_automation.models.datasource.interfaces;

import cds_automation.enums.datasource.AssessableIncomeStatus;

import java.math.BigDecimal;

public interface ICustomIncomeRecord {
  String getNaturalId();

  BigDecimal getAssessableIncome();

  AssessableIncomeStatus getAssessableIncomeStatus();
}
