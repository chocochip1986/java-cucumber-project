package cdit_automation.data_helpers;

import cdit_automation.configuration.StepDefLevelTestContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class MhaBulkFileDataPrep extends BatchFileDataPrep {

    public List<String> createBodyOfTestScenarios(List<Map<String, String>> list, StepDefLevelTestContext testContext) {
        List<String> body = new ArrayList<>();



        return body;
    }
}
