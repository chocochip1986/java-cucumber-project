package cdit_automation.pages;

import cdit_automation.asserts.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CdsPage extends AbstractPage {
    public final String loginUrl = "/login";
    public final String LOGIN_BTN_TO_FORM = "#btn_login";
    public final String LOGIN_USERNAME = "#username";
    public final String LOGIN_PASSWORD = "#password";
    public final String LOGIN_BTN = "#kc-login";

    public final String DATASOURCE_BTN = "#datasource-button";

    public void visitLogin() {
        visit("/login");
    }

    public void visit(String url) {
        if ( url ==  null ) {
          url = "";
        }
        url = "http://"+testManager.getTestEnvironment().getDatasourceUiUrl()+":"+testManager.getTestEnvironment().getDatasourceUiPort()+url;
        log.info("Visiting webpage at: "+url);
        driverManager.getDriver().get(url);
        verifyLoginPage();
    }

    public void clickLoginBtn() {
        pageUtils.click_on(LOGIN_BTN);
        Assert.assertTrue(pageUtils.hasElement(DATASOURCE_BTN), "Unable to login to CDS Intranet!");
    }

    public void fillInLoginForm() {
        pageUtils.setText(LOGIN_USERNAME, testManager.getTestEnvironment().getDatasourceUiUsername());
        pageUtils.setText(LOGIN_PASSWORD, testManager.getTestEnvironment().getDatasourceUiPassword());
    }

    public void clickLoginBtnToForm() {
        pageUtils.click_on(LOGIN_BTN_TO_FORM);
        verifyLoginForm();
        pageUtils.click_on(LOGIN_BTN);
    }

    public void verifyLoginForm() {
        Assert.assertTrue(pageUtils.hasElement(LOGIN_USERNAME), "Cds Intranet Login Form - Username field not found!");
        Assert.assertTrue(pageUtils.hasElement(LOGIN_PASSWORD), "Cds Intranet Login Form - Password field not found!");
    }

    public void verifyLoginPage() {
        Assert.assertTrue(pageUtils.hasElement(LOGIN_BTN_TO_FORM), "Cds Intranet Login button not found!" );
    }
}
