package cdit_automation.configuration.datasource;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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

    public <T> T replace(String name, T newObject) {
        if ( contains(name) ) {
            try {
                testVariables.replace(name, newObject);
            } catch (RuntimeException e) {
                String erroMsg = "Unable to replace variable in the TestContext: { key: "+name+", value: "+newObject.getClass().toString();
                erroMsg+="\n"+e.getMessage();
                log.error(erroMsg);
                throw new RuntimeException(erroMsg);
            }
        } else {
            String erroMsg = "Nothing to replace variable in the TestContext: { key: "+name+", value: "+newObject.getClass().toString();
            log.error(erroMsg);
            throw new RuntimeException(erroMsg);
        }
        return (T)testVariables.get(name);
    }

    public boolean contains(String key) {
        return testVariables.containsKey(key);
    }

    public boolean doNotContain(String key) {
        return !testVariables.containsKey(key);
    }

    public <T> T remove(String key) {
        return (T)testVariables.remove(key);
    }

    public void flush() {
        testVariables = new HashMap<>();
    }
}
