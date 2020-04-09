package automation.asserts;

import static org.assertj.core.api.Assertions.assertThat;

import automation.configuration.TestManager;
import automation.driver_management.DriverManager;
import automation.exceptions.TestFailException;
import java.io.File;
import java.io.IOException;
import java.util.function.Supplier;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.assertj.core.groups.Tuple;
import org.openqa.selenium.OutputType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Assert {
    @Autowired
    DriverManager driverManager;

    @Autowired
    TestManager testManager;

    public void assertNotNull(Object t, String errorMsg) {
        if ( t == null ) {
            raiseError(errorMsg);
        }
    }

    public void assertNull(Object t, String errorMsg) {
        if ( t != null ) {
            raiseError(errorMsg);
        }
    }

    public void assertTrue(Boolean actual, String errorMsg) {
        if ( !actual ) {
            String finalMsg = errorMsg+"\nExpected: true"+"\nActual: "+actual.toString();
            raiseError(finalMsg);
        }
    }

    public void assertFalse(Boolean actual, String errorMsg) {
        if ( actual ) {
            String finalMsg = errorMsg+"\nExpected: true"+"\nActual: "+actual.toString();
            raiseError(finalMsg);
        }
    }

    public void assertEquals (String expected, String actual, String errorMsg) {
        if ( !actual.equals(expected) ) {
            String finalMsg = errorMsg+"\nExpected: "+expected+"\nActual: "+actual;
            raiseError(finalMsg);
        }
    }

    public void assertNotEquals(String expected, String actual, String errorMsg) {
        if ( actual.equals(expected) ) {
            String finalMsg = errorMsg+"\nExpected: "+expected+"\nActual: "+actual;
            raiseError(finalMsg);
        }
    }

    public void assertEquals(Object expected, Object actual, String errorMsg) {
        if ( !actual.equals(expected) ) {
            String finalMsg = errorMsg+"\nExpected: "+expected.toString()+"\nActual: "+actual.toString();
            raiseError(finalMsg);
        }
    }

    public void assertEquals(int expected, int actual, String errorMsg) {
        if (!(expected==actual)) {
            String finalMsg = String.format("%s\nExpected: %d\nActual: %d", errorMsg, expected, actual);
            raiseError(finalMsg);
        }
    }

    public void assertEquals(Object expected, Object actual, Supplier<String> generateErrorMessage ) {
        if ( !actual.equals(expected) ) {
            String finalMsg = generateErrorMessage.get();
            finalMsg += System.lineSeparator()+"Expected: "+expected.toString()+System.lineSeparator()+"Actual: "+actual.toString();
            raiseError(finalMsg);
        }
    }

    public void assertEquals(Tuple expected, Object actual, String errorMsg, String... values) {
        try {
            assertThat(actual).extracting(values).containsExactly(expected);
        } catch (Exception e) {
            errorMsg += System.lineSeparator()+"Expected: "+expected.toString()+System.lineSeparator()+"Actual: "+actual.toString();
            raiseError(errorMsg);
        }
    }

    public void assertNotEquals(Object expected, Object actual, String errorMsg) {
        if ( actual.equals(expected) ) {
            String finalMsg = errorMsg+"\nExpected: "+expected.toString()+"\nActual: "+actual.toString();
            raiseError(finalMsg);
        }
    }

    public void assertEquals(long expected, long actual, String errorMsg) {
        if (expected != actual) {
            String finalMsg = String.format("%s\nExpected: %d\nActual: %d", errorMsg, expected, actual);
            raiseError(finalMsg);
        }
    }

    private void raiseError(String errorMessage) {
        errorMessage(errorMessage);
        throw new TestFailException(errorMessage);
    }

    private void raiseErrorAndTakeScreenshot(String errorMessage) {
        errorMessage(errorMessage);
        takeScreenshot();
        throw new TestFailException(errorMessage);
    }

    private void errorMessage(String errorMessage) {
        log.error(errorMessage);
    }

    private void takeScreenshot() {
        File srcFile = testManager.takeScreenshot(OutputType.FILE);
        File destFile = new File(testManager.getTestResultsDir()+File.separator+"screenshot01.jpg");
        try {
            FileUtils.copyFile(srcFile, destFile);
        } catch ( IOException e ) {
            String errorMsg = "Unable to save screenshot from "+srcFile.getAbsolutePath()+" to "+destFile.getAbsolutePath();
            errorMsg += "\n"+e.getClass().toString()+": "+e.getStackTrace();
            throw new TestFailException(errorMsg);
        }
    }
}
