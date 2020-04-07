package cds_automation.enums.datasource;

public enum BrowserTypeEnums {
    CHROME("chrome");
//    FIREFOX("firefox"),
//    IE("ie"),
//    EDGE("edge"),
//    SAFARI("safari"),
//    OPERA("opera");

    private String browserName;

    BrowserTypeEnums(String name) {
        this.browserName = name;
    }
}
