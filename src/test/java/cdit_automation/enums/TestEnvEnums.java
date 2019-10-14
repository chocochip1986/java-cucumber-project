package cdit_automation.enums;

public enum TestEnvEnums {
    LOCAL("local", "hostname", "testusername", "testpassword", "1521", "XE", 10L);

    private String name;
    private String databaseHostName;
    private String databaseUsername;
    private String databasePassword;
    private String databasePort;
    private String databaseName;
    private long implicitWait;

    TestEnvEnums(String name, String databaseHostName, String databaseUsername, String databasePassword, String databasePort, String databaseName, long implicitWait) {
        this.name = name;
        this.databaseHostName = databaseHostName;
        this.databaseUsername = databaseUsername;
        this.databasePassword = databasePassword;
        this.databasePort = databasePort;
        this.databaseName = databaseName;
        this.implicitWait = implicitWait;
    }

    public long getImplicitWait() {
        return implicitWait;
    }
}
