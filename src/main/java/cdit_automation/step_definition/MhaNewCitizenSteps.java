package cdit_automation.step_definition;

import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.FileTypeEnum;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import java.util.List;

public class MhaNewCitizenSteps extends AbstractSteps {
  @Given("^a (MHA_NEW_CITIZEN) file with the following details$")
  public void aMHA_NEW_CITIZENFileWithTheFollowingDetails(FileTypeEnum fileTypeEnum, DataTable dataTable) {
    List<String> formattedEntries = this.mhaNewCitizenFileDataPrep.formatEntries(dataTable);
    mhaNewCitizenFileDataPrep.writeToFile(formattedEntries);
  }

  @Given("^a MHA_NEW_CITIZEN file contains a (.*) invalid date of run$")
  public void aMHA_NEW_CITIZENFileContainsAnInvalidDateOfRun(String problem, DataTable dataTable) {
    if (problem.equals("FutureDate")) {
      problem = dateUtils.daysAfterToday(1).format(Phaker.DATETIME_FORMATTER_YYYYMMDD);
    }
    List<String> formattedEntries = mhaNewCitizenFileDataPrep.formatEntries(dataTable);
    mhaNewCitizenFileDataPrep.writeToFile(problem, formattedEntries);
  }
}
