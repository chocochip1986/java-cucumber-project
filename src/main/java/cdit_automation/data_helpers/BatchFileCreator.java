package cdit_automation.data_helpers;

import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.FileDetail;
import cdit_automation.models.FileReceived;
import cdit_automation.utilities.FileUtils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;

@Component
public class BatchFileCreator extends AbstractFileCreator {
    public FileReceived replaceFile(FileDetail fileDetail, String fileName) {
        File file = FileUtils.replaceFile(testManager.getOutputArtifactsDir()+File.separator+fileName+".txt");

        FileReceived fileReceived = FileReceived.builder()
                .receivedTimestamp(Timestamp.valueOf(LocalDateTime.now()))
                .filePath(file.getAbsolutePath())
                .fileDetail(fileDetail)
                .build();

        fileReceivedRepo.save(fileReceived);

        return fileReceived;
    }

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

    public FileReceived createFileReceived(FileDetail fileDetail, String fileName, Timestamp timestamp) {
        File file = FileUtils.findOrCreate(testManager.getOutputArtifactsDir()+File.separator+fileName+".txt");

        FileReceivedDataDto fileReceivedDataDto = FileReceivedDataDto.createOk(fileDetail, file, timestamp);

        apiHelper.sendCallToCreateFileReceivedRecord(fileReceivedDataDto);

        boolean isFound = waitUntilCondition(new Supplier<Boolean>(){
            public Boolean get() {
                FileReceived fileReceived = fileReceivedRepo.findByFileDetailIdAndFileStatusEnumAndHash(fileReceivedDataDto.getFileDetailId(), fileReceivedDataDto.getFileStatusEnum(), fileReceivedDataDto.getHash());
                if (fileReceived != null) {
                    return Boolean.TRUE;
                } else {
                    return Boolean.FALSE;
                }
            }
        });

        if ( !isFound ) {
            throw new TestFailException("Unable to find a File Received record corresponding to the following information: "+fileReceivedDataDto.toString());
        }

        FileReceived fileReceived = fileReceivedRepo.findByFileDetailIdAndFileStatusEnumAndHash(fileReceivedDataDto.getFileDetailId(), fileReceivedDataDto.getFileStatusEnum(), fileReceivedDataDto.getHash());

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
