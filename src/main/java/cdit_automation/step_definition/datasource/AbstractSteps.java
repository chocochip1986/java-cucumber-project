package cdit_automation.step_definition.datasource;

import cdit_automation.configuration.AbstractAutoWired;
import cdit_automation.enums.datasource.FileTypeEnum;
import io.cucumber.java.ParameterType;

public class AbstractSteps extends AbstractAutoWired {
    @ParameterType("DUAL CITIZEN|NEW CITIZEN|BULK CITIZEN|CHANGE ADDRESS")
    protected FileTypeEnum fileType(String fileType) {
        return FileTypeEnum.fromString(fileType);
    }
}
