package cdit_automation.data_helpers.datasource;

import cdit_automation.constants.TestConstants;
import cdit_automation.data_helpers.datasource.factories.PersonFactory;
import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.datasource.InvalidDateOfRunEnum;
import cdit_automation.enums.datasource.InvalidNricEnum;
import cdit_automation.models.datasource.Batch;
import cdit_automation.repositories.datasource.BatchRepo;
import cdit_automation.repositories.datasource.CeasedCitizenRepo;
import cdit_automation.repositories.datasource.NationalityRepo;
import cdit_automation.repositories.datasource.PersonDetailRepo;
import cdit_automation.repositories.datasource.PersonIdRepo;
import cdit_automation.repositories.datasource.PersonNameRepo;
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
        switch(type) {
            case EMPTY:
                return "";
            case EMPTY_SPACE:
                return "        ";
            case INVALID_FORMAT:
                return TestConstants.DEFAULT_EXTRACTION_DATE.format(dateUtils.DATETIME_FORMATTER_DDMMYYYY);
            case FUTURE_DATE:
                return LocalDate.now().plusDays(5L).format(dateUtils.DATETIME_FORMATTER_YYYYMMDD);
            default :
                return null;
        }
    }

    public String generateInvalidNric(InvalidNricEnum type) {
        switch (type) {
            case EMPTY:
                return "";
            case EMPTY_SPACE:
                return  "         ";
            case INVALID:
                return createInvalidNric();
            case SHORT:
                return createValidNric().substring(0, 8);
            case S555:
                return "S5550000B";
            case S888:
                return "S8880001Z";
            default :
                return null;
        }
    }
}
