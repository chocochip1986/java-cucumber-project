package cdit_automation.step_definition;

import cdit_automation.configuration.AbstractAutoWired;
import cdit_automation.enums.FileTypeEnum;
import cdit_automation.exceptions.TestFailException;
import io.cucumber.java.ParameterType;
import io.cucumber.java.Scenario;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class AbstractSteps extends AbstractAutoWired {
    @ParameterType("DUAL CITIZEN|NEW CITIZEN|BULK CITIZEN|CHANGE ADDRESS")
    protected FileTypeEnum fileType(String fileType) {
        return FileTypeEnum.fromString(fileType);
    }

    protected void takeScreenshot(String screenshotName) {
        if ( testManager.isBrowserOpened() ) {
            File srcFile = testManager.takeScreenshot();
            File destFile = new File(testManager.getTestResultsDir()+File.separator+screenshotName+".jpg");
            try {
                FileUtils.copyFile(srcFile, destFile);
            } catch ( IOException e ) {
                String errorMsg = "Unable to save screenshot from "+srcFile.getAbsolutePath()+" to "+destFile.getAbsolutePath();
                errorMsg += "\n"+e.getClass().toString()+": "+e.getStackTrace();
                throw new TestFailException(errorMsg);
            }
        }
    }

    protected String screenshotNameMaker(Scenario scenario) {
        return scenario.getName().replace(" ", "_");
    }
}
