package cdit_automation.pages;

import cdit_automation.configuration.AbstractAutoWired;
import cdit_automation.driver_management.DriverManager;
import cdit_automation.page_navigation.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractPage extends AbstractAutoWired {
    @Autowired
    DriverManager driverManager;
}
