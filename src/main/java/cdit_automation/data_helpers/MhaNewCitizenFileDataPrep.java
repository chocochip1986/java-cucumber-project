package cdit_automation.data_helpers;

import cdit_automation.data_helpers.batch_entities.MhaNewCitizenFileEntry;
import cdit_automation.enums.FileTypeEnum;
import io.cucumber.datatable.DataTable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class MhaNewCitizenFileDataPrep extends BatchFileDataPrep {
  public List<String> formatEntries(DataTable dataTable) {
    List<Map<String, String>> dataRows = dataTable.asMaps(String.class, String.class);
    List<MhaNewCitizenFileEntry> entries =
        dataRows.stream().map(MhaNewCitizenFileEntry::new).collect(Collectors.toList());
    return entries.stream().map(MhaNewCitizenFileEntry::toRawString).collect(Collectors.toList());
  }

  public void writeToFile(List<String> entries) {
    this.writeToFile(generateSingleHeader(LocalDate.now()), entries);
  }
  
  public void writeToFile(String dateStr, List<String> entries) {
    batchFileDataWriter.begin(dateStr, FileTypeEnum.MHA_NEW_CITIZEN, null);
    entries.forEach(line -> batchFileDataWriter.chunkOrWrite(line));
    batchFileDataWriter.end();
  }
}
