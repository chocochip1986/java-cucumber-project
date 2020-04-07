package cds_automation.pages;

import cds_automation.configuration.datasource.AbstractAutoWired;
import cds_automation.driver_management.DriverManager;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractPage extends AbstractAutoWired {
    @Autowired
    DriverManager driverManager;
}
