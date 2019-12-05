package cdit_automation.step_definition;

import cdit_automation.exceptions.TestFailException;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;

@Slf4j
@Ignore
public class IrasAiBulkSteps extends AbstractSteps {
    @Then("the IRAS AI bulk file should be created")
    public void theIRASAIBulkFileShouldBeCreated() {
        log.info("Verify that the IRAS AI bulk file is created...");
        Path filePath = testContext.get("filePath");
        boolean isFound = waitUntilCondition(new Supplier<Boolean>(){
            public Boolean get() {
                if (Paths.get(filePath.toString(), "input_bulk_assessable_incom.txt").toFile().exists()) {
                    return Boolean.TRUE;
                } else {
                    return Boolean.FALSE;
                }
            }
        });

        if ( !isFound ) {
            throw new TestFailException("The file input_bulk_assessable_incom.txt does not exists at "+filePath.toAbsolutePath().toString());
        }
    }
}
