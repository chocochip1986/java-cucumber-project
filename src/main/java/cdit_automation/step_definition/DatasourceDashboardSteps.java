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

    @And("^I access datasource ui$")
    public void iAccessDatasourceUi() {
        log.info("");
    }
}
