package cdit_automation.step_definition;

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
}
