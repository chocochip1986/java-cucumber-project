package cdit_automation.driver_management;


public class DriverManager {
    private static DriverManager SINGLE_INSTANCE = null;

    private DriverManager() {
        initialize();
    }

    public static DriverManager instance() {
        if ( SINGLE_INSTANCE == null ) {
            SINGLE_INSTANCE = new DriverManager();
        }
        return SINGLE_INSTANCE;
    }

    private void initialize() {

    }
}
