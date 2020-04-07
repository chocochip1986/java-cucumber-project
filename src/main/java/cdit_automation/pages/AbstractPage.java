package cdit_automation.pages;

import cdit_automation.configuration.datasource.AbstractAutoWired;
import cdit_automation.driver_management.DriverManager;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractPage extends AbstractAutoWired {
    @Autowired
    DriverManager driverManager;
}
