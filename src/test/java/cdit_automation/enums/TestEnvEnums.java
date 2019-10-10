package cdit_automation.enums;

public enum TestEnvEnums {
    LOCAL("local", "hostname", "testusername", "testpassword", "1521", "XE");

    private String name;
    private String databaseHostName;
    private String databaseUsername;
    private String databasePassword;
    private String databasePort;
    private String databaseName;

    TestEnvEnums(String name, String databaseHostName, String databaseUsername, String databasePassword, String databasePort, String databaseName) {
        this.name = name;
        this.databaseHostName = databaseHostName;
        this.databaseUsername = databaseUsername;
        this.databasePassword = databasePassword;
        this.databasePort = databasePort;
        this.databaseName = databaseName;
    }
}
