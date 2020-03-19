package cdit_automation.asserts;

import cdit_automation.configuration.TestManager;
import cdit_automation.driver_management.DriverManager;
import cdit_automation.exceptions.TestFailException;
import java.io.File;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
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

    public void assertNotEquals(Object expected, Object actual, String errorMsg) {
        if ( actual.equals(expected) ) {
            String finalMsg = errorMsg+"\nExpected: "+expected.toString()+"\nActual: "+actual.toString();
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
        File srcFile = ((TakesScreenshot)driverManager.getDriver()).getScreenshotAs(OutputType.FILE);
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
