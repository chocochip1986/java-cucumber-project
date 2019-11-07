package cdit_automation.data_helpers;

import cdit_automation.enums.FileTypeEnum;
import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.FileDetail;
import cdit_automation.models.FileReceived;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Component
public class BatchFileCreator extends AbstractFileCreator {
    private final String absoluteFilePath = "src/test/resources/artifacts/";

    public FileReceived fileCreator(FileDetail fileDetail, String fileName) {
//        FileDetail fileDetail = fileDetailRepo.findByFileEnum(FileTypeEnum.MHA_DUAL_CITIZEN);
        File file = new File(absoluteFilePath+fileName+".txt");
        if ( !file.exists() ) {
            throw new TestFailException("No such file in path "+absoluteFilePath+fileName+".txt");
        }
        FileReceived fileReceived = FileReceived.builder()
                .receivedTimestamp(Timestamp.valueOf(LocalDateTime.now()))
                .filePath(file.getAbsolutePath())
                .fileDetail(fileDetail)
                .build();

        fileReceivedRepo.save(fileReceived);

        return fileReceived;
    }

    public String generateDoubleHeader(@Nullable LocalDate extractionDate, @Nullable LocalDate cutOffDate) {
        if ( extractionDate == null ) {
            extractionDate = dateUtils.daysBeforeToday(5);
        }
        if ( cutOffDate == null ) {
            cutOffDate = dateUtils.daysBeforeToday(5);
        }

        return extractionDate.format(dateUtils.DATETIME_FORMATTER_YYYYMMDD)+cutOffDate.format(dateUtils.DATETIME_FORMATTER_YYYYMMDD);
    }

    public String generateDoubleHeader() {
        return generateDoubleHeader(null, null);
    }

    public void writeToFile(String fileName, List<String> lines) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(absoluteFilePath+fileName));
        for ( int i = 0 ; i < lines.size() ; i++ ) {
            writer.write(lines.get(i));
            if ( i+1 != lines.size() ) {
                writer.write(System.lineSeparator());
            }
        }
        writer.close();
    }
}
