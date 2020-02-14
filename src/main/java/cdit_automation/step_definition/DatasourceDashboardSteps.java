package cdit_automation.step_definition;

import cdit_automation.pages.CdsPage;
import io.cucumber.java.en.And;
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

    @And("^I access datasource ui$")
    public void iAccessDatasourceUi() {
        log.info("");
    }
}
