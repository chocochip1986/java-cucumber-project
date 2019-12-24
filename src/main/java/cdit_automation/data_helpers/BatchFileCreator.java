package cdit_automation.data_helpers;

import cdit_automation.models.FileDetail;
import cdit_automation.models.FileReceived;
import cdit_automation.utilities.FileUtils;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class BatchFileCreator extends AbstractFileCreator {
    public FileReceived fileCreator(FileDetail fileDetail, String fileName) {
        File file = FileUtils.findOrCreate(testManager.getOutputArtifactsDir()+File.separator+fileName+".txt");

        FileReceived fileReceived = FileReceived.builder()
                .receivedTimestamp(Timestamp.valueOf(LocalDateTime.now()))
                .filePath(file.getAbsolutePath())
                .fileDetail(fileDetail)
                .build();

        fileReceivedRepo.save(fileReceived);

        return fileReceived;
    }

    public void writeToFile(String fileName, List<String> lines) throws IOException {
        File file = FileUtils.findOrCreate(testManager.getOutputArtifactsDir()+File.separator+fileName+".txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsolutePath()));
        for ( int i = 0 ; i < lines.size() ; i++ ) {
            writer.write(lines.get(i));
            if ( i+1 != lines.size() ) {
                writer.write(System.lineSeparator());
            }
        }
        writer.close();
    }
}
