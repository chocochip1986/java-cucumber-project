package cdit_automation.data_helpers;

import cdit_automation.data_setup.Phaker;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MhaDeathDateFileDataPrep extends BatchFileDataPrep {

    public List<String> createListWithInvalidNrics(int numOfInvalidNrics) {
        List<String> listOfInvalidNrics = new ArrayList<>();

        for ( int i = 0 ; i < numOfInvalidNrics ; i++ ) {
            listOfInvalidNrics.add(Phaker.invalidNric()+Phaker.validPastDate().format(Phaker.DATETIME_FORMATTER_YYYYMMDD));
        }

        return listOfInvalidNrics;
    }
}
