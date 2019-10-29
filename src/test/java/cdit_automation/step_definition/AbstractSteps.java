package cdit_automation.step_definition;

import cdit_automation.configuration.StepDefLevelTestContext;
import cdit_automation.configuration.TestManager;
import cdit_automation.data_helpers.BatchFileCreator;
import cdit_automation.data_helpers.PersonIdService;
import cdit_automation.page_navigation.PageUtils;
import cdit_automation.repositories.BatchRepo;
import cdit_automation.repositories.ErrorMessageRepo;
import cdit_automation.repositories.FileDetailRepo;
import cdit_automation.repositories.FileReceivedRepo;
import cdit_automation.repositories.IncomingRecordRepo;
import cdit_automation.repositories.NationalityRepo;
import cdit_automation.repositories.PersonIdRepo;
import cdit_automation.repositories.PersonRepo;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractSteps {

    @Autowired protected TestManager testManager;

    @Autowired protected PageUtils pageUtils;

    @Autowired protected StepDefLevelTestContext testContext;

    @Autowired protected BatchFileCreator batchFileCreator;

    @Autowired
    protected PersonIdService personIdService;

    @Autowired protected BatchRepo batchRepo;
    @Autowired protected FileReceivedRepo fileReceivedRepo;
    @Autowired protected FileDetailRepo fileDetailRepo;
    @Autowired protected IncomingRecordRepo incomingRecordRepo;
    @Autowired protected NationalityRepo nationalityRepo;
    @Autowired protected PersonRepo personRepo;
    @Autowired protected ErrorMessageRepo errorMessageRepo;
    @Autowired protected PersonIdRepo personIdRepo;
}
