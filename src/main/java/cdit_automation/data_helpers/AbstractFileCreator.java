package cdit_automation.data_helpers;

import cdit_automation.api_helpers.ApiHelper;
import cdit_automation.configuration.TestManager;
import cdit_automation.repositories.datasource.FileDetailRepo;
import cdit_automation.repositories.datasource.FileReceivedRepo;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractFileCreator extends AbstractService {

    @Autowired TestManager testManager;

    @Autowired
    protected FileDetailRepo fileDetailRepo;

    @Autowired
    protected FileReceivedRepo fileReceivedRepo;

    @Autowired
    protected ApiHelper apiHelper;

    protected boolean waitUntilCondition(Supplier<Boolean> function) {
        Double timer = 0.0;
        Double maxWaitDuration = 10.0;
        boolean isFound;
        do {
            isFound = function.get();
            if ( isFound ) {
                break;
            } else {
                testManager.sleep();
                progressBar(timer);
                timer++;
            }
        } while ( timer < maxWaitDuration+1.0 );
        System.out.println(System.lineSeparator());
        return isFound;
    }

    private void progressBar(double timer) {
        StringBuilder stringCount = new StringBuilder(30);
        stringCount
                .append("\r")
                .append(String.format("  Waited for %d seconds...", (int)timer));
        System.out.print(stringCount);
    }
}
