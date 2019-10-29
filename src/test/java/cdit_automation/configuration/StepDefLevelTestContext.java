package cdit_automation.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class StepDefLevelTestContext {

    private Map<String, Object> testVariables;

    public StepDefLevelTestContext () {
        init();
    }

    private void init() {
        testVariables = new HashMap<>();
    }

    public <T> T get(String name) {
        try {
            if ( name == null ) {
                throw new RuntimeException();
            }
            return (T) testVariables.get(name);
        }
        catch ( NullPointerException e ) {
            String errorMsg = "Unable to retrieve any value from the TestContext with a null key!";
            errorMsg+="\n"+e.getMessage();
            log.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }
    }

    public <T> T set(String name, T object) {
        try {
            if (testVariables.containsKey(name)) {
                throw new RuntimeException();
            }
            else {
                testVariables.put(name, object);
            }
        } catch ( RuntimeException e ) {
            String erroMsg = "Unable to add variable into the TestContext: { key: "+name+", value: "+object.getClass().toString();
            erroMsg+="\n"+e.getMessage();
            log.error(erroMsg);
            throw new RuntimeException(erroMsg);
        }
        return (T)testVariables.get(name);
    }

    public void reset() {
        testVariables = new HashMap<>();
    }
}
