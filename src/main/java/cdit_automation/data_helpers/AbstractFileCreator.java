package cdit_automation.data_helpers;

import cdit_automation.configuration.TestManager;
import cdit_automation.repositories.FileDetailRepo;
import cdit_automation.repositories.FileReceivedRepo;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractFileCreator extends AbstractService {

    @Autowired TestManager testManager;

    @Autowired
    protected FileDetailRepo fileDetailRepo;

    @Autowired
    protected FileReceivedRepo fileReceivedRepo;
}
