package cdit_automation.utilities;

import cdit_automation.exceptions.TestFailException;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.util.List;

public class FileUtils {
    public static void replaceLineInFile(String absolutePathToFile, String newLine, long lineNumber) {
        replaceLineInFile(new File(absolutePathToFile), newLine, lineNumber);
    }

    public static void replaceLineInFile(File targetFile, String newLine, long lineNumber) {
        try {
            RandomAccessFile file = new RandomAccessFile(targetFile, "rw");
            file.seek(lineNumber);
            file.write((newLine+System.lineSeparator()).getBytes());
            file.close();
        } catch (IOException e) {
            String errorMsg = "Issues replacing line at "+lineNumber+" of file: "+targetFile.getAbsolutePath();
            errorMsg += System.lineSeparator()+e.getStackTrace();
            throw new TestFailException(errorMsg);
        }
    }

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
