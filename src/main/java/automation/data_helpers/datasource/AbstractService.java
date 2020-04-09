package automation.data_helpers.datasource;

import automation.repositories.datasource.BatchRepo;
import automation.repositories.datasource.ErrorMessageRepo;
import automation.repositories.datasource.FileDetailRepo;
import automation.repositories.datasource.FileReceivedRepo;
import automation.repositories.datasource.GenderRepo;
import automation.repositories.datasource.IncomingRecordRepo;
import automation.repositories.datasource.NationalityRepo;
import automation.repositories.datasource.PersonDetailRepo;
import automation.repositories.datasource.PersonIdRepo;
import automation.repositories.datasource.PersonNameRepo;
import automation.repositories.datasource.PersonRepo;
import automation.utilities.DateUtils;
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
