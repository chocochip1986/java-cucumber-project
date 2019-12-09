package cdit_automation.asserts;

import static org.assertj.core.api.Assertions.assertThat;

import cdit_automation.exceptions.TestFailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Assert {
    public static void assertNotNull(Object t, String errorMsg) {
        if ( t == null ) {
            raiseError(errorMsg);
        }
    }

    public static void assertNull(Object t, String errorMsg) {
        if ( t != null ) {
            raiseError(errorMsg);
        }
    }

    public static void assertTrue (String expected, String actual, String errorMsg) {
        if ( !actual.equals(expected) ) {
            raiseError(errorMsg);
        }
    }

    public static void assertFalse(String expected, String actual, String errorMsg) {
        if ( actual.equals(expected) ) {
            raiseError(errorMsg);
        }
    }

    public static void assertEquals(Object expected, Object actual, String errorMsg) {
        if ( !actual.equals(expected) ) {
            String finalMsg = errorMsg+"\nExpected: "+expected.toString()+"\nActual: "+actual.toString();
            raiseError(finalMsg);
        }
    }

    private static void raiseError(String errorMessage) {
        errorMessage(errorMessage);
        throw new TestFailException(errorMessage);
    }

    private static void raiseErrorAndTakeScreenshot(String errorMessage) {
        errorMessage(errorMessage);
        takeScreenshot();
        throw new TestFailException(errorMessage);
    }

    private static void errorMessage(String errorMessage) {
        log.error(errorMessage);
    }

    private static void takeScreenshot() {
//        File srcFile = ((TakesScreenshot)driverManager.getDriver()).getScreenshotAs(OutputType.FILE);
    }
}
