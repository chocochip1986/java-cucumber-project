package cdit_automation.utilities;

import cdit_automation.exceptions.TestFailException;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

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

    public static File replaceFile(String absolutePathToFile) {
        File file = new File(absolutePathToFile);
        try {
            Files.deleteIfExists(file.toPath());
            file.createNewFile();
        } catch ( IOException e ) {
            throw new TestFailException("Unable to replace file at path: "+absolutePathToFile);
        }
        return file;
    }

    public static void writeChunkToFile(@NotNull File file, @NotNull List<String> lines) {
        if ( file == null || lines == null ) {
            throw new TestFailException("File or input line cannot be null!");
        }
        try {
            if ( !file.exists() ) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file, true);
            for(String line : lines) {
                fos.write(line.getBytes());
            }
            fos.close();
        } catch ( IOException e ) {
            throw new TestFailException("Unable to write to file at path: "+file.getAbsolutePath());
        }
    }

    public static File createAndWriteToFile(File file, String line) {
        try {
            Files.deleteIfExists(file.toPath());
            FileOutputStream fos = new FileOutputStream(file, true);
            fos.write(line.getBytes());
            fos.close();
        } catch ( IOException e ) {
            throw new TestFailException("Unable to replace file at path: "+file.getAbsolutePath());
        }
        return file;
    }

    public static void writeToFile(@NotNull File file, @NotNull String line) {
        if ( file == null || line == null ) {
            throw new TestFailException("File or input line cannot be null!");
        }
        try {
            if ( !file.exists() ) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file, true);
            fos.write(line.getBytes());
            fos.close();
        } catch ( IOException e ) {
            throw new TestFailException("Unable to create file at path: "+file.getAbsolutePath());
        }
    }
}
