package cdit_automation.step_definition;

import cdit_automation.pages.CdsPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Ignore
public class DatasourceDashboardSteps extends AbstractSteps {
    @Autowired
    CdsPage cdsPage;

    @And("^I access CDS Intranet$")
    public void iAccessCdsIntranet() {
        log.info("Visiting CDS Intranet...");
        cdsPage.visitLogin();
    }

    @And("^I login to CDS Intranet as a CPF officer$")
    public void iLoginCdsIntranetAsCpfOfficer() {
        log.info("Visiting CDS Intranet...");
        cdsPage.visitLogin();
        log.info("Login...");
        cdsPage.clickLoginBtnToForm();
        cdsPage.fillInLoginForm();
        cdsPage.clickLoginBtn();
    }

    @And("^I access Datasource UI (Files Dashboard|Data Enquiry|Error Rate) function$")
    public void iAccessDatasourceUi(String datasourceSubLink) {
        log.info("Accessing "+datasourceSubLink+" function in Datasource UI...");
        cdsPage.accessDatasourceUI(datasourceSubLink);
    }

    @And("I logout of CDS Intranet")
    public void iLogoutOfCDSIntranet() {
        log.info("Logging out of CDS Intranet...");
        cdsPage.logOff();
    }

    @Then("I should remain as logged out")
    public void iShouldRemainAsLoggedOut() {
        cdsPage.verifyLoginPage();
    }
}
