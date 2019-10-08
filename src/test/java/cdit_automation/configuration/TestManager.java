package cdit_automation.configuration;

public class TestManager {
    private static TestManager SINGLE_INSTANCE = null;

    private TestManager() {
        initialize();
    }

    public static TestManager getInstance() {
        if ( SINGLE_INSTANCE == null ) {
            SINGLE_INSTANCE = new TestManager();
        }
        return SINGLE_INSTANCE;
    }

    private void initialize() {

    }
}
