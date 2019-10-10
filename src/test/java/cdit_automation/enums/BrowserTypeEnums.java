package cdit_automation.enums;

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
