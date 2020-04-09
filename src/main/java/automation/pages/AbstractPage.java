package automation.pages;

import automation.configuration.datasource.AbstractAutoWired;
import automation.driver_management.DriverManager;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractPage extends AbstractAutoWired {
    @Autowired
    DriverManager driverManager;
}
