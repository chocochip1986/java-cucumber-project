package cds_automation.data_helpers.datasource;

import cds_automation.repositories.datasource.BatchRepo;
import cds_automation.repositories.datasource.ErrorMessageRepo;
import cds_automation.repositories.datasource.FileDetailRepo;
import cds_automation.repositories.datasource.FileReceivedRepo;
import cds_automation.repositories.datasource.GenderRepo;
import cds_automation.repositories.datasource.IncomingRecordRepo;
import cds_automation.repositories.datasource.NationalityRepo;
import cds_automation.repositories.datasource.PersonDetailRepo;
import cds_automation.repositories.datasource.PersonIdRepo;
import cds_automation.repositories.datasource.PersonNameRepo;
import cds_automation.repositories.datasource.PersonRepo;
import cds_automation.utilities.DateUtils;
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
