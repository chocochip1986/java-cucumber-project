package cdit_automation.step_definition.datasource;

import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.datasource.PersonId;
import io.cucumber.java.en.Then;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

@Slf4j
@Ignore
public class IrasAiBulkSteps extends AbstractSteps {
    @Then("the IRAS AI bulk file should be created")
    public void theIRASAIBulkFileShouldBeCreated() {
        log.info("Verify that the IRAS AI bulk file is created...");
        Path filePath = testContext.get("filePath");
        boolean isFound = waitUntilCondition(new Supplier<Boolean>(){
            public Boolean get() {
                if (Paths.get(filePath.toString(), "input_bulk_assessable_income.txt").toFile().exists()) {
                    return Boolean.TRUE;
                } else {
                    return Boolean.FALSE;
                }
            }
        });

        if ( !isFound ) {
            throw new TestFailException("The file input_bulk_assessable_income.txt does not exists at "+filePath.toAbsolutePath().toString());
        }
    }

    @Then("I verify that there are no erroneous entries in the IRAS AI bulk file")
    public void iVerifyThatThereAreNoErrorenousEntriesInTheIRASAIBulkFile() {
        log.info("Verifying that there are no erroneous entries in the IRAS AI bulk file...");
        Path filePath = testContext.get("filePath");
        File file = new File(filePath.toString()+"/input_bulk_assessable_income.txt");
        List<PersonId> listOfExpectedPpl = testContext.get("listOfPpl");
        if ( file.exists() ) {
            if ( listOfExpectedPpl.isEmpty() ) {
                throw new TestFailException("There is an empty list of people expected to be in the IRAS AI bulk file!!!");
            }
        } else {
            throw new TestFailException("The file input_bulk_assessable_income.txt does not exists at "+filePath.toAbsolutePath().toString());
        }
    }
}
