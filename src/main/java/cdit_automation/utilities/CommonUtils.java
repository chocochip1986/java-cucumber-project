package cdit_automation.utilities;

import cdit_automation.constants.TestConstants;
import cdit_automation.data_setup.Phaker;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonUtils {
    
    public static String emptyStringIfInputIsKeywordBlank(String detailString) {

        if (TestConstants.OPTION_BLANK.equalsIgnoreCase(detailString)) {
            return StringUtils.EMPTY_STRING;
        }

        return detailString;
    }

    public static String nricFieldOptions(String option) {

        String nricValue;

        switch(option.toUpperCase()) {
            case TestConstants.OPTION_VALID:
                nricValue = Phaker.validNric();
                break;
            case TestConstants.OPTION_INVALID:
                nricValue = Phaker.invalidNric();
                break;
            case TestConstants.OPTION_SPACES:
                nricValue = StringUtils.rightPad(StringUtils.SPACE, 9);
                break;
            case TestConstants.OPTION_BLANK:
                nricValue = StringUtils.EMPTY_STRING;
                break;
            default:
                nricValue = option;
        }
        return nricValue;
    }

    public static String nameFieldOptions(String option) {

        String name;

        switch (option.toUpperCase()) {
            case TestConstants.OPTION_VALID:
                name = Phaker.validName();
                break;
            case TestConstants.OPTION_SPACES:
                name = StringUtils.rightPad(StringUtils.SPACE, 66);
                break;
            case TestConstants.OPTION_BLANK:
                name = StringUtils.EMPTY_STRING;
                break;
            default:
                name = option;
        }
        return name;
    }
}
