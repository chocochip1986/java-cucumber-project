package cdit_automation.data_helpers;

import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Component
public class BatchFileCreator extends AbstractFileCreator {
    private final String absoluteFilePath = "src/test/resources/artifacts/";

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
