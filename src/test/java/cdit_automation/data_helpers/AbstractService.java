package cdit_automation.data_helpers;

import cdit_automation.repositories.BatchRepo;
import cdit_automation.repositories.ErrorMessageRepo;
import cdit_automation.repositories.FileDetailRepo;
import cdit_automation.repositories.FileReceivedRepo;
import cdit_automation.repositories.IncomingRecordRepo;
import cdit_automation.repositories.NationalityRepo;
import cdit_automation.repositories.PersonDetailRepo;
import cdit_automation.repositories.PersonIdRepo;
import cdit_automation.repositories.PersonRepo;
import cdit_automation.utilities.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractService {
    @Autowired protected DateUtils dateUtils;

    @Autowired protected PersonIdRepo personIdrepo;
    @Autowired protected PersonRepo personRepo;
    @Autowired protected NationalityRepo nationalityRepo;
    @Autowired protected BatchRepo batchRepo;
    @Autowired protected FileDetailRepo fileDetailRepo;
    @Autowired protected FileReceivedRepo fileReceivedRepo;
    @Autowired protected IncomingRecordRepo incomingRecordRepo;
    @Autowired protected ErrorMessageRepo errorMessageRepo;
    @Autowired protected PersonDetailRepo personDetailRepo;
}
