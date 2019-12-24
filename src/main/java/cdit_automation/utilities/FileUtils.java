package cdit_automation.utilities;

import cdit_automation.exceptions.TestFailException;

import java.io.File;
import java.io.IOException;

public class FileUtils {
    public static File findOrCreate(String absolutePathToFile) {
        File file = new File(absolutePathToFile);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch ( IOException e ) {
            throw new TestFailException("Unable to create file at path: "+absolutePathToFile);
        }
        return file;
    }
}
