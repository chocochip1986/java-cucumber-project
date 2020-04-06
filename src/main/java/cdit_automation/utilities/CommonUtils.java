package cdit_automation.utilities;

import cdit_automation.constants.TestConstants;
import cdit_automation.data_setup.Phaker;
import cdit_automation.exceptions.TestFailException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonUtils {
    
    public static String emptyStringIfInputIsKeywordBlank(String detailString) {

        if (TestConstants.OPTION_BLANK.equalsIgnoreCase(detailString)) {
            return StringUtils.EMPTY_STRING;
        }

        return detailString;
    }

    public static String nricFieldOptions(String nricOption) {

        String nricValue = "";

        switch(nricOption.toUpperCase()) {
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
                if (nricOption.matches("^[STFG]\\d{7}[A-Z]$")) {
                    nricValue = nricOption;
                } else {
                    throw new TestFailException("Unsupported option when building a file entry nric with following option: " + nricOption);
                }
        }
        return nricValue;
    }
}
