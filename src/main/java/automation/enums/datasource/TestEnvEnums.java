package automation.enums.datasource;

import lombok.Getter;

@Getter
@Deprecated
public enum TestEnvEnums {
    LOCAL("local", "hostname", "testusername", "testpassword", "1521", "XE",
            "localhost", "8081", 1L, 10L);

    private String name;

    //Environment specifics for database
    private String databaseHostName;
    private String databaseUsername;
    private String databasePassword;
    private String databasePort;
    private String databaseName;

    //Environment specifics for datasource service connection
    private String datasourceUrl;
    private String datasourcePort;

    private long implicitWait;
    private long explicitWait;

    TestEnvEnums(String name, String databaseHostName, String databaseUsername, String databasePassword, String databasePort, String databaseName,
                 String datasourceUrl, String datasourcePort, long implicitWait, long explicitWait) {
        this.name = name;
        this.databaseHostName = databaseHostName;
        this.databaseUsername = databaseUsername;
        this.databasePassword = databasePassword;
        this.databasePort = databasePort;
        this.databaseName = databaseName;

        this.datasourceUrl = datasourceUrl;
        this.datasourcePort = datasourcePort;

        this.implicitWait = implicitWait;
        this.explicitWait = explicitWait;
    }

    public long getImplicitWait() {
        return implicitWait;
    }

    public long getExplicitWait() {
        return explicitWait;
    }
}
