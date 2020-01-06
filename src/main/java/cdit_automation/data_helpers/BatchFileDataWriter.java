package cdit_automation.data_helpers;

import cdit_automation.configuration.TestManager;
import cdit_automation.enums.FileTypeEnum;
import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.FileDetail;
import cdit_automation.repositories.FileDetailRepo;
import cdit_automation.utilities.FileUtils;
import cdit_automation.utilities.StringUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Getter
@Slf4j
@Component
public class BatchFileDataWriter {

    @Autowired TestManager testManager;
    @Autowired
    FileDetailRepo fileDetailRepo;

    private final int DEFAULT_CHUNK_SIZE = 1000;

    private String header;
    private List<String> chunkedBody;
    private int finalBodyLength;
    private int chunkSize;
    private String filename;
    private File destionationFile;
    private boolean isInstantiated;
    private FileDetail fileDetail;

    @Autowired
    public BatchFileDataWriter() {
        header = null;
        chunkedBody = new ArrayList<>();
        finalBodyLength = 0;
        chunkSize = DEFAULT_CHUNK_SIZE;
        filename = null;
        destionationFile = null;
        isInstantiated = true;
        fileDetail = null;
    }

    public void begin(@NotNull String header, @NotNull FileTypeEnum fileTypeEnum, @Nullable Integer chunkSize) {
        if ( header == null || fileTypeEnum == null ) {
            throw new TestFailException("Batch file header string cannot be null.\nFile name string cannot be null");
        }
        this.header = addLineSeparatorAtEndOfStringIfMissing(header);
        this.filename = fileTypeEnum.getValue().toLowerCase();
        this.chunkSize = chunkSize == null ? DEFAULT_CHUNK_SIZE : chunkSize;
        this.finalBodyLength = 0;
        this.fileDetail = retreiveFileDetailsFor(fileTypeEnum);

        setupDestinationFile();
        FileUtils.writeToFile(this.destionationFile, this.header);
        this.isInstantiated = true;
    }

    public void chunkOrWrite(String line) {
        if ( destionationFile == null ) {
            throw new TestFailException("No file to write stuff into!");
        }
        chunkedBody.add(addLineSeparatorAtEndOfStringIfMissing(line));
        if ( chunkedBody.size() >= chunkSize ) {
            log.info("Writing chunk of "+chunkedBody.size()+" lines to file at: "+destionationFile.getAbsolutePath());
            writeAndResetChunk();
        }
    }

    public void end() {
        if ( destionationFile == null ) {
            throw new TestFailException("No file to write stuff into!");
        }
        writeAndResetChunk();
        if ( fileDetail.getFooter() ) {
            String footerLine = "";
            if ( fileDetail.getHasFooterFiller() ) {
                footerLine = StringUtils.leftPad(String.valueOf(finalBodyLength), fileDetail.getFooterFillerSize());
            } else {
                footerLine = String.valueOf(finalBodyLength);
            }
            FileUtils.writeToFile(destionationFile, footerLine);
        }
    }

    public void reset(){
        header = null;
        chunkedBody = new ArrayList<>();
        finalBodyLength = 0;
        chunkSize = 0;
        filename = null;
        destionationFile = null;
    }

    private void writeAndResetChunk() {
        FileUtils.writeChunkToFile(destionationFile, chunkedBody);
        finalBodyLength+=chunkedBody.size();
        chunkedBody = new ArrayList<>();
    }

    private FileDetail retreiveFileDetailsFor(FileTypeEnum fileTypeEnum) {
        try {
            return fileDetailRepo.findByFileEnum(fileTypeEnum);
        } catch ( RuntimeException e ) {
            throw new TestFailException("Unable to retrieve file details information for file: "+fileTypeEnum.getValue());
        }
    }

    private void setupDestinationFile() {
        this.destionationFile = FileUtils.findOrCreate(testManager.getOutputArtifactsDir()+File.separator+this.filename+".txt");
    }

    private String addLineSeparatorAtEndOfStringIfMissing(String line) {
        Pattern patten = Pattern.compile("\\.*"+System.lineSeparator()+"$");
        if ( !patten.matcher(line).find() ) {
            return line+System.lineSeparator();
        } else {
            return line;
        }
    }
}
