package cdit_automation.data_helpers;

import cdit_automation.constants.TestConstants;
import cdit_automation.data_helpers.factories.PersonFactory;
import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.InvalidDateOfRunEnum;
import cdit_automation.models.Batch;
import cdit_automation.repositories.BatchRepo;
import cdit_automation.repositories.CeasedCitizenRepo;
import cdit_automation.repositories.NationalityRepo;
import cdit_automation.repositories.PersonDetailRepo;
import cdit_automation.repositories.PersonIdRepo;
import cdit_automation.repositories.PersonNameRepo;
import cdit_automation.utilities.DateUtils;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;

public class BatchFileDataPrep {

    @Autowired protected DateUtils dateUtils;
    @Autowired protected PersonFactory personFactory;

    @Autowired protected PersonDetailRepo personDetailRepo;
    @Autowired protected PersonNameRepo personNameRepo;
    @Autowired protected BatchRepo batchRepo;
    @Autowired protected CeasedCitizenRepo ceasedCitizenRepo;
    @Autowired protected PersonIdRepo personIdRepo;
    @Autowired protected NationalityRepo nationalityRepo;

    @Autowired protected BatchFileDataWriter batchFileDataWriter;

    protected int parseStringSize(String size) {
        try {
            return Integer.valueOf(size);
        } catch ( NumberFormatException e ) {
            //Fail silently
        }
        return 0;
    }

    public String createValidNric() {
        return Phaker.validNric();
    }

    public String createInvalidNric() {
        return Phaker.invalidNric();
    }

    public String generateDoubleHeader(@Nullable LocalDate extractionDate, @Nullable LocalDate cutOffDate) {
        if ( extractionDate == null ) {
            extractionDate = TestConstants.DEFAULT_EXTRACTION_DATE;
        }
        if ( cutOffDate == null ) {
            cutOffDate = TestConstants.DEFAULT_CUTOFF_DATE;
        }

        return extractionDate.format(dateUtils.DATETIME_FORMATTER_YYYYMMDD)+cutOffDate.format(dateUtils.DATETIME_FORMATTER_YYYYMMDD);
    }

    public String generateSingleHeader(@Nullable LocalDate extractionDate) {
        if ( extractionDate == null ) {
            extractionDate = TestConstants.DEFAULT_EXTRACTION_DATE;
        }
        return extractionDate.format(dateUtils.DATETIME_FORMATTER_YYYYMMDD);
    }

    public String generateSingleHeader() {
        return generateSingleHeader(null);
    }

    public String generateDoubleHeader() {
        return generateDoubleHeader(null, null);
    }

    public Batch generateAndSaveBatch() {
        
        Batch batch = new Batch();
        batch.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        batchRepo.save(batch);
        
        return batch;
    }

    public String generateInvalidSingleHeader(InvalidDateOfRunEnum type) {
        if(type.equals(InvalidDateOfRunEnum.EMPTY)) {
            return "";
        }

        if(type.equals(InvalidDateOfRunEnum.SPACE)) {
            return "        ";
        }

        if(type.equals(InvalidDateOfRunEnum.INVALID_FORMAT)) {
            LocalDate runDate =  TestConstants.DEFAULT_EXTRACTION_DATE;
            return runDate.format(dateUtils.DATETIME_FORMATTER_DDMMYYYY);
        }

        if(type.equals(InvalidDateOfRunEnum.FUTURE_DATE)) {
            LocalDate runDate =  LocalDate.now().plusDays(1);
            return runDate.format(dateUtils.DATETIME_FORMATTER_YYYYMMDD);
        }

        return "";
    }
}
