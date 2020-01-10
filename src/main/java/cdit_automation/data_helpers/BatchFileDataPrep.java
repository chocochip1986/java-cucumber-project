package cdit_automation.data_helpers;

import cdit_automation.constants.TestConstants;
import cdit_automation.data_helpers.factories.PersonFactory;
import cdit_automation.data_setup.Phaker;
import cdit_automation.repositories.BatchRepo;
import cdit_automation.repositories.CeasedCitizenRepo;
import cdit_automation.repositories.NationalityRepo;
import cdit_automation.repositories.PersonDetailRepo;
import cdit_automation.repositories.PersonIdRepo;
import cdit_automation.repositories.PersonNameRepo;
import cdit_automation.utilities.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    public List<String> createListOfInvalidNrics(int size) {
        List<String> listOfInvalidNrics = new ArrayList<>();
        for ( int i = 0 ; i < size ; i++ ) {
            listOfInvalidNrics.add(Phaker.invalidNric());
        }

        return listOfInvalidNrics;
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

    public String generateDoubleHeader() {
        return generateDoubleHeader(null, null);
    }
}
