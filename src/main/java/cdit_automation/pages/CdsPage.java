package cdit_automation.pages;

import cdit_automation.asserts.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CdsPage extends AbstractPage {
    public final String loginUrl = "/login";
    public final String loginPageBtn = "#btn_login";

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

    public void verifyLoginPage() {
        Assert.assertTrue(pageUtils.hasElement(loginPageBtn), "Cds Intranet Login button not found!" );
    }
}
