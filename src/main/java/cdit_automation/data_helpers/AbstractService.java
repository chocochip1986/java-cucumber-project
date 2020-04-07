package cdit_automation.data_helpers;

import cdit_automation.repositories.datasource.BatchRepo;
import cdit_automation.repositories.datasource.ErrorMessageRepo;
import cdit_automation.repositories.datasource.FileDetailRepo;
import cdit_automation.repositories.datasource.FileReceivedRepo;
import cdit_automation.repositories.datasource.GenderRepo;
import cdit_automation.repositories.datasource.IncomingRecordRepo;
import cdit_automation.repositories.datasource.NationalityRepo;
import cdit_automation.repositories.datasource.PersonDetailRepo;
import cdit_automation.repositories.datasource.PersonIdRepo;
import cdit_automation.repositories.datasource.PersonNameRepo;
import cdit_automation.repositories.datasource.PersonRepo;
import cdit_automation.utilities.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractService {
    @Autowired protected DateUtils dateUtils;

    @Autowired protected PersonIdRepo personIdrepo;
    @Autowired protected PersonRepo personRepo;
    @Autowired protected PersonNameRepo personNameRepo;
    @Autowired protected NationalityRepo nationalityRepo;
    @Autowired protected BatchRepo batchRepo;
    @Autowired protected FileDetailRepo fileDetailRepo;
    @Autowired protected FileReceivedRepo fileReceivedRepo;
    @Autowired protected IncomingRecordRepo incomingRecordRepo;
    @Autowired protected ErrorMessageRepo errorMessageRepo;
    @Autowired protected PersonDetailRepo personDetailRepo;
    @Autowired protected GenderRepo genderRepo;
}
