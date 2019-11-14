package cdit_automation.data_helpers;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MhaDeathDateFileDataPrep extends BatchFileDataPrep {

    public List<String> createListWithInvalidNrics(int numOfInvalidNrics) {
        List<String> listOfInvalidNrics = new ArrayList<>();

        return listOfInvalidNrics;
    }
}
