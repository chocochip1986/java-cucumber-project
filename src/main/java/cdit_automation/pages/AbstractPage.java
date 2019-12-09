package cdit_automation.pages;

import cdit_automation.driver_management.DriverManager;
import cdit_automation.page_navigation.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractPage {
    @Autowired
    PageUtils pageUtils;

    @Autowired
    DriverManager driverManager;
}
